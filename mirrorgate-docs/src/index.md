# MirrorGate

MirrorGate is a WallBoard application meant to give teams fast feedback in all the different areas related to software development.

![MirrorGate](./assets/img/logo.png)

## Why that name?

MirrorGate is meant to display relevant information on how your software looks like from every perspective, from the planning to the user’s feedback. So it's the mirror where teams can see their work reflected, helping them to perform self-criticism and continuous improvement. It aims to improve software quality and time-to-market by making the team aware of how the software it is developing and its process looks like.

That said, to be honest, it all comes from this dialog from `The Never Ending Story`:

* Engywook: Next is the Magic Mirror Gate. Atreyu has to face his true self.
* Falcor: So what? That won't be too hard for him.
* Engywook: Oh, that's what everyone thinks! But kind people find out that they are cruel. Brave men discover that they are really cowards! Confronted by their true selves, most men run away screaming!

# Functionality

Right now MirrorGate offers different dashboard types and views:

## Detail dashboard

Offers information on:
* Sprint advance status and backlog refinement.
* Program Increment (PI) advance status.
* Incidences visualization by criticality.
* Build status per repository.
* Build statistics and failure tendency.
* Marketplace feedback for mobile applications in iTunes, PlayStore or [directly captured](#feedback).
* Active users from Google Analytics and Adobe Analytics.
* AWS operation metrics.
* Alerts.
* Slack notifications.

![ScreenCatpure](assets/img/main.png)

We expect to be adding much more information in the near future so stay tuned.

## Aggregate dashboard

It also offers a view where you can display several product dashboards in a single view with a more summed up information.

![ScreenCatpure](assets/img/aggregate.png)


## Backoffice

Additionally, MirrorGate offers a backoffice application where dashboards can be configured.

![ScreenCatpure](assets/img/backoffice.png)


# Architecture

In order to operate, MirrorGate requires several components:

- Main MirrorGate application: typically executed by using the `bbva-ae/mirrorgate` docker container.
- MongoDB database. Should be bound to the application by using the `SPRING_DATA_MONGODB_URI` environment variable (for example `SPRING_DATA_MONGODB_URI=mongodb://localhost:27017/dashboarddb`).
- [Collectors](https://github.com/BBVA?utf8=%E2%9C%93&q=mirrorgate%20collector&type=&language=): collectors are components in charge of seeking and pushing information to the application. Currently, the following collectors exist:
    - Jenkins plugin: is a plugin that pushes information from a Jenkins CI server.
    - Jira collector: is a standalone application that polls Jira servers for changes every configurable amount of time.
    - Market collector: is a standalone process that polls smartphone applications marketplaces for user reviews.

# Security

MirrorGate currently doesn't provide any authentication mechanism so if you want to secure it you will have to deploy it behind a reverse proxy.

It provides 3 different authorization depending on the value of the `X-Forwarded-User` header provided by the reverse proxy:

- If the value of the header is `ANONYMOUS`, the access will be annonymous. The user will have read only access to the dashboards. This is meant to be use to place the dashboard in screens without the need of user authentication.
- If the header is not set or equals `COLLECTOR` access to the `/api` endpoints will be granted, allowing the source system to push information. This is typically meant to be used for collectors and that's why it allows the header not to be provided in case you rely in perimetral security and the collectors access MirrorGate from behind the reverse proxy.
- For any other case, the header is taken as the user id. It will have access to dashboards both write and read.

> If you wan to completely disable the security in MirrorGate you can execute it with the env variable `SPRING_PROFILES_ACTIVE=embedded`.

# Collecting feedback

MirrorGate ecosystem includes a markets-collector to be able to fetch feedback directly from the iOS and Android App Stores. Eventhough, it also offers an [endpoint](./collect-feedback.md) that might be invoked to directly send feedback on an specific product (directly from the front via form post or ajax request or from server side).

# Supported browsers

In MirrorGate we use some edge HTML and CSS features, thus only latest Chrome and Firefox versions are supported at the moment (i.e. IE and Safari are not currently supported).
