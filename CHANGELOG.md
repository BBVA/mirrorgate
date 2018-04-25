<a name=0.3.0></a>
# [0.3.0](https://github.com/BBVA/mirrorgate/compare/v0.2.0...v0.3.0) (2018-04-25)


### Bug Fixes

* **e2e-tests:** fix e2e tests to work with docker containers properly ([eddce60](https://github.com/BBVA/mirrorgate/commit/eddce60))
* **metrics:** resolve historic operational metrics regex match expression ([e5847ca](https://github.com/BBVA/mirrorgate/commit/e5847ca))
* **operations:** display 0 metrics values properly ([420200a](https://github.com/BBVA/mirrorgate/commit/420200a), [cce2a47](https://github.com/BBVA/mirrorgate/commit/cce2a47))


### Features

* **backoffice:** aggregated dashboard list input shows a dropdown menu with the name of available dashboards. ([566758f](https://github.com/BBVA/mirrorgate/commit/566758f))
* **backoffice:** provide an interface to configure dashboard's content by drag amd drop ([feeeb43](https://github.com/BBVA/mirrorgate/commit/feeeb43))
* **current-sprint:** add info to current-sprint component. ([4cd3263](https://github.com/BBVA/mirrorgate/commit/4cd3263))
* **dashboard:** add release notes icon. ([0f8a809](https://github.com/BBVA/mirrorgate/commit/0f8a809))
* **docs:** add CHANGELOG.md to about page ([c583c60](https://github.com/BBVA/mirrorgate/commit/c583c60))
* **search:** make search work for both name and display name ([7a0d9aa](https://github.com/BBVA/mirrorgate/commit/7a0d9aa))


<a name=0.2.0></a>
# [0.2.0](https://github.com/BBVA/mirrorgate/compare/v0.1.0...v0.2.0) (2018-03-16)


### Bug Fixes

* **backoffice:** application versions input is set on Markets Widget ([4bbf2b2](https://github.com/BBVA/mirrorgate/commit/4bbf2b2))
* **bugs:** bugs does not get displayed if using something different than product name ([fbd5487](https://github.com/BBVA/mirrorgate/commit/fbd5487))
* **dashboard:** avoid make a request when a service has not timer ([8198f4d](https://github.com/BBVA/mirrorgate/commit/8198f4d))
* **docs:** resolve navigation errors ([6d1be4e](https://github.com/BBVA/mirrorgate/commit/6d1be4e))
* **metrics:** show only last metric received for activeUsers, weeklyUsers and infrastructureCost metrics ([b6279a8](https://github.com/BBVA/mirrorgate/commit/b6279a8))
* **multi-product:** resolve duplication of dashboards when resize ([63f271a](https://github.com/BBVA/mirrorgate/commit/63f271a))
* **multi-product:** do not display deleted dashboard on multi-product dashboards ([4dfd026](https://github.com/BBVA/mirrorgate/commit/4dfd026))
* **next-sprint:** control zero, NaN and undefined data ([04132f1](https://github.com/BBVA/mirrorgate/commit/04132f1))
* **notifications:** return the http status code 424 when slack is not set properly ([0102052](https://github.com/BBVA/mirrorgate/commit/0102052))
* **notifications:** show slack edited messages ([a1e4ca2](https://github.com/BBVA/mirrorgate/commit/a1e4ca2))
* **operations:** only check previous notification of the own instance of operations-metrics component. This was not working properly in multi-product dashboards. ([0382010](https://github.com/BBVA/mirrorgate/commit/0382010))
* **program-increment:** JavaScript error when a PI does not exists ([1011b66](https://github.com/BBVA/mirrorgate/commit/1011b66))
* **reviews:** comments rotation speed ([e7e9850](https://github.com/BBVA/mirrorgate/commit/e7e9850))
* **reviews:** resume comments rotation when timeout is over ([ea2ac17](https://github.com/BBVA/mirrorgate/commit/ea2ac17))
* **time-to-master:** round commits per day to two decimals ([0ad87ad](https://github.com/BBVA/mirrorgate/commit/0ad87ad))
* **user-metrics:** JavaScript error when a user-metrics does not exists ([eb8e58e](https://github.com/BBVA/mirrorgate/commit/eb8e58e))
* **user-metrics:** show active users metrics when those are also zero. ([052202a](https://github.com/BBVA/mirrorgate/commit/052202a))

### Features

* **backoffice:** add an element when you press space, enter or unfocus from input list elements ([5de73b8](https://github.com/BBVA/mirrorgate/commit/5de73b8))
* **backoffice:** make elements of input list draggable ([16d07f1](https://github.com/BBVA/mirrorgate/commit/16d07f1))
* **backoffice:** add general help link. ([34582fc](https://github.com/BBVA/mirrorgate/commit/34582fc))
* **backoffice:** add navigation to pagination and search ([c688a9c](https://github.com/BBVA/mirrorgate/commit/c688a9c))
* **backoffice:** separate user analytics and operation analytics in backoffice ([103defe](https://github.com/BBVA/mirrorgate/commit/103defe))
* **bugs:** bugs component shows alert when there are critical or major bugs and warning when there are medium bugs. ([75c50cc](https://github.com/BBVA/mirrorgate/commit/75c50cc))
* **builds:** simple-builds-tile also send notifications. ([ce12b12](https://github.com/BBVA/mirrorgate/commit/ce12b12))
* **dashboard:** add search box of dashboards ([1b34842](https://github.com/BBVA/mirrorgate/commit/1b34842))
* **dashboard:** modify simple and detail for customizable dashboards structure ([c510687](https://github.com/BBVA/mirrorgate/commit/c510687))
* **docs:** add about page for aws metrics ([f7ec481](https://github.com/BBVA/mirrorgate/commit/f7ec481))
* **issues:** new mongo script for cleaning old unnecessary issues from database([b2bbf30](https://github.com/BBVA/mirrorgate/commit/b2bbf30))
* **metrics:** display metric tendencies ([29510ea](https://github.com/BBVA/mirrorgate/commit/29510ea))
* **metrics:** display aws costs of infrastructure ([d7190ea](https://github.com/BBVA/mirrorgate/commit/d7190ea))
* **multi-product:** add Slack notifications and operations metrics component to multi-product dashboards. ([c80f6d7](https://github.com/BBVA/mirrorgate/commit/c80f6d7))
* **multi-product:** join user metrics and  feedback component in multi-product dashboards. ([c80f6d7](https://github.com/BBVA/mirrorgate/commit/c80f6d7))
* **next-sprint:** highlight backlog component status ([7ded25e](https://github.com/BBVA/mirrorgate/commit/7ded25e))
* **notification:** throw desktop notifications when something important happens ([bf3d5ae](https://github.com/BBVA/mirrorgate/commit/bf3d5ae))
* **operations:** display metric tendencies for operational metrics. ([972b0e7](https://github.com/BBVA/mirrorgate/commit/972b0e7))
* **operations:** display operational metrics for AWS ApiGateway.
* **operations:** display operational metrics for AWS ELB.
* **scm-metrics:** display the average of commits per day from the last 30 days. ([7aced63](https://github.com/BBVA/mirrorgate/commit/7aced63))
* **scm-metrics:** display time-to-master stats ([2563704](https://github.com/BBVA/mirrorgate/commit/2563704))
* **user-metrics:** display short term tendency for active users ([44af63b](https://github.com/BBVA/mirrorgate/commit/44af63b))
* **user-metrics:** display seven users metrics tendency ([49dd43d](https://github.com/BBVA/mirrorgate/commit/49dd43d))

# 0.1.0

Initial version with following features:

* Sprint advance status and backlog refinement.
* Program Increment (PI) advance status.
* Incidences visualization by criticality.
* Build status per repository.
* Build statistics and failure tendency.
* Marketplace feedback for mobile applications in iTunes, PlayStore or directly captured.
* Active users from Google Analytics and Adobe Analytics.
* AWS operation metrics.
* Alerts.
* Slack notifications.
* Multi-product dashboards with simplified components.

