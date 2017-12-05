![MirrorGate](../mirrorgate-docs/src/assets/img/logo.png)

![license](https://img.shields.io/github/license/BBVA/mirrorgate.svg)

# Scripts & Utilities

These Scripts aim to help MirrorGate development and deployment helping to consolidate stored data, cleaning old data or building and running MirrorGate in different environments.

* buildAll.sh: build all project modules at the same time.
* buildAndRun.sh: build and run all project modules at the same time.
* mongoDump.sh: is a utility for creating a dump of the database with current date extension. Very useful for maintaining a backup strategy. Needs to set following environment variables:
  *  MONGO_HOST: mongo host (127.0.0.1 by default).
  *  MONGO_PORT: mongo port (27017 by default).
  *  MONGO_AUTHDB: mongo database to be dumped.
  *  MONGO_USER: mongo username with Read Access to MONGO_AUTHDB.
  *  MONGO_PASS: mongo password for MONGO_USER.

  It also allows two parameters for uploading the result dump to a S3 Bucket of AWS, i.e.

  ```sh
  ./mongoDump.sh --secrets-file {{ secrets_file }} --bucket {{ backups_bucket }}
  ```

### Mongo Scripts

* remove_old_events.js: remove events documents older than 1 day from database.
* remove_old_not_latest_builds.js: remove not latest builds documents older than 3 months from database.
* clean_mirrorgate_db.js: run previous scripts to clean mirrorgate database from old documents.

# Contributing

Please read the [contributing guide](../CONTRIBUTING.md).

# Credit

This project is inspired by [CapitalOne's Hygieia](https://github.com/capitalone/Hygieia).