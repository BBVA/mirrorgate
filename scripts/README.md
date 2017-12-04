![MirrorGate](../mirrorgate-docs/src/assets/img/logo.png)

![license](https://img.shields.io/github/license/BBVA/mirrorgate.svg)

# Scripts & Utilities

These Scripts aim to help MirrorGate development and deployment helping to consolidate stored data, cleaning old data or building and running MirrorGate in different environments.

* buildAll.sh: build all project modules at the same time.
* buildAndRun.sh: build and run all project modules at the same time.

### Mongo Scripts

* remove_old_events.js: remove events documents older than 1 day from database.
* remove_old_not_latest_builds.js: remove not latest builds documents older than 3 months from database.
* clean_mirrorgate_db.js: run previous scripts to clean mirrorgate database from old documents.

# Contributing

Please read the [contributing guide](./CONTRIBUTING.md).

# Credit

This project is inspired by [CapitalOne's Hygieia](https://github.com/capitalone/Hygieia).