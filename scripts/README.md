![MirrorGate](../mirrorgate-docs/src/assets/img/logo.png)

![license](https://img.shields.io/github/license/BBVA/mirrorgate.svg)

# Scripts & Utilities

These Scripts aim to help MirrorGate development and deployment helping to consolidate stored data, cleaning old data or building and running MirrorGate in different environments.

* buildAll.sh: build all project modules at the same time.
* buildAndRun.sh: build and run all project modules at the same time.
* mongoDump.sh: this utility create a dump of the database with current date extension only if it is executed in a secondary node of a replica set environment. Very useful for maintaining a backup strategy. It needs to set following environment variables:
  *  MONGO_HOST: mongo host (127.0.0.1 by default).
  *  MONGO_PORT: mongo port (27017 by default).
  *  MONGO_AUTHDB: mongo database to be dumped.
  *  MONGO_USER: mongo username with Read Access to MONGO_AUTHDB.
  *  MONGO_PASS: mongo password for MONGO_USER.

  It also allows two parameters for uploading resulted dump to a S3 Bucket of AWS, i.e.

  ```sh
  ./mongoDump.sh --secrets-file {{ secrets_file }} --bucket {{ backups_bucket }}
  ```
* mongoCleanUp.sh: this utility run clean up mongo scripts only if it is executed in a secondary node of a replica set environment. Environment variables:
  *  MONGO_HOST: mongo host (127.0.0.1 by default).
  *  MONGO_PORT: mongo port (27017 by default).
  *  MONGO_AUTHDB: mongo database to be dumped.
  *  MONGO_USER: mongo username with Read Access to MONGO_AUTHDB.
  *  MONGO_PASS: mongo password for MONGO_USER.

### Mongo Scripts

* remove_old_events.js: remove events documents older than 1 day from database.
* remove_old_issues.js: remove unnecessary issues older than 3 months from database.
* remove_old_not_latest_builds.js: remove not latest builds documents older than 3 months from database.
* remove_old_commits.js: remove commits with date of merge to master branch older than 3 months from database.
* remove_old_user_metrics.js: remove user metrics older than 3 months from database.
* remove_old_historic_user_metrics.js: remove historic user metrics older than 3 months from database.
* clean_mirrorgate_db.js: run previous scripts to clean mirrorgate database from old documents.

# Contributing

Please read the [contributing guide](../CONTRIBUTING.md).

# Credit

This project is inspired by [CapitalOne's Hygieia](https://github.com/capitalone/Hygieia).