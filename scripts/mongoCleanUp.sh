#!/bin/bash

IM_SECONDARY=`mongo --quiet --eval 'rs.isMaster().secondary'`
if [[ $? -ne 0 ]]; then echo "Error al comprobar nodo de mongodb."; exit 1; fi

if [[ $IM_SECONDARY == "false" ]];
then
    echo "El clean up de la base de datos no se ejecuta en este nodo (no es secundario)."
    exit 0
fi

if [ -z ${MONGO_HOST} ]; then export MONGO_HOST="127.0.0.1"; fi
if [ -z ${MONGO_PORT} ]; then export MONGO_PORT="27017"; fi

# Dump the database
echo "Cleaning up database..."
if [ -z $MONGO_USER ]
  then
    mongo $MONGO_HOST:$MONGO_PORT/$MONGO_DB < ./clean_mirrorgate_db.js
  else
    mongo $MONGO_HOST:$MONGO_PORT -u $MONGO_USER -p $MONGO_PASS --authenticationDatabase $MONGO_AUTHDB < ./clean_mirrorgate_db.js
fi
if [[ $? -ne 0 ]]; then echo "Clean up database error ($MONGO_HOST:$MONGO_PORT)."; exit 1; fi
echo "Done."
