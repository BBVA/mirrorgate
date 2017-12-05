#!/bin/bash

IM_SECONDARY=`mongo --quiet --eval 'rs.isMaster().secondary'`
if [[ $? -ne 0 ]]; then echo "Error al comprobar nodo de mongodb."; exit 1; fi

if [ $IM_SECONDARY == "false" ];
then
    echo "El backup no se ejecuta en este nodo (no es secundario)."
    exit 0
fi

OPTIONS=$(getopt --options "s:b:" --longoptions "secrets-file:,bucket:" -- "$@")
if [[ $? -ne 0 ]]; then echo "Error al obtener los argumentos."; exit 1; fi
eval set -- "$OPTIONS"

while [[ $# -gt 0 ]];
do
    case $1 in
        -s|--secrets-file)
            SECRETS_FILE=$2
            shift 2
            ;;
        -b|--bucket)
            BUCKET=$2
            shift 2
            ;;

        --)
            shift
            break
            ;;

        *)
            break
            ;;
    esac
done

TIMESTAMP=`date -u +%Y%m%d`
BACKUP_NAME="mongodump_$TIMESTAMP"

if [ -z ${MONGO_HOST} ]; then export MONGO_HOST="127.0.0.1"; fi
if [ -z ${MONGO_PORT} ]; then export MONGO_PORT="27017"; fi

# Load the file with database secrets
echo "Loading database credentials..."
eval $(aws s3 cp ${SECRETS_FILE} - | sed 's/^/ /')
if [[ $? -ne 0 ]]; then echo "Error al cargar credenciales desde el bucket $SECRETS_FILE."; exit 1; fi
echo "Done."

# Dump the database
echo "Dumping database..."
if [ -z "$MONGO_USER" ]
  then
    mongodump -h $MONGO_HOST:$MONGO_PORT -d $MONGO_DB --out $BACKUP_NAME
  else
    mongodump -h $MONGO_HOST:$MONGO_PORT -u $MONGO_USER -p $MONGO_PASS --authenticationDatabase $MONGO_DB --out $BACKUP_NAME --oplog
fi
if [[ $? -ne 0 ]]; then echo "Error al hacer dump de la base de datos ($MONGO_HOST:$MONGO_PORT)."; exit 1; fi
echo "Done."

# Archive and compress data
echo "Archiving and compressing data..."
tar -czvf $BACKUP_NAME.tgz $BACKUP_NAME
if [[ $? -ne 0 ]]; then echo "Error al crear el tgz."; rm -fr $BACKUP_NAME; exit 1; fi
echo "Done."

# Upload backup to s3 bucket
if [ -z ${$BUCKET} ]
    then
        echo "Uploading backup to s3..."
        aws s3 cp $BACKUP_NAME.tgz $BUCKET/mongodb/$BACKUP_NAME.tgz
        if [[ $? -ne 0 ]]; then echo "Error al subir $BACKUP_NAME.tgz a $BUCKET/mongodb/$BACKUP_NAME.tgz."; rm -fr $BACKUP_NAME; rm -fr $BACKUP_NAME.tgz; exit 1; fi
        echo "Done."
fi

# Prepare last dump directory for restoring
echo "Preparing last dump directory for restoring..."
rm -fr lastdump/
mkdir lastdump
cp -R $BACKUP_NAME/* lastdump/
echo "Done."

# Clean directory
echo "Cleaning directory..."
rm -fr $BACKUP_NAME
if [[ $? -ne 0 ]]; then echo "Error al borrar el directorio $BACKUP_NAME."; exit 1; fi
rm -fr $BACKUP_NAME.tgz
if [[ $? -ne 0 ]]; then echo "Error al borrar $BACKUP_NAME.tgz."; exit 1; fi
echo "Done."
