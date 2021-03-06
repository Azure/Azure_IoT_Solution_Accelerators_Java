[![Build][build-badge]][build-url]
[![Issues][issues-badge]][issues-url]
[![Gitter][gitter-badge]][gitter-url]

# Iot Hub Manager Overview

This service manages most of Azure IoT Hub interactions, such as creating and managing IoT devices, device twins, invoking Methods, and tagging devices. This service is also used to run queries to retrieve devices belonging to a particular group (defined by the user).

This microservice provides a RESTful endpoint to manage devices, device twins, commands, methods and all the Azure IoT Hub features required by the [Azure IoT Remote Monitoring](https://github.com/Azure/azure-iot-pcs-remote-monitoring-java) project.

## Why?

This microservice was built as part of the [Azure IoT Remote Monitoring](https://github.com/Azure/azure-iot-pcs-remote-monitoring-java) project to provide a generic implementation for an end-to-end IoT solution. More information [here][rm-arch-url].

## Features

* Device creation in IoT Hub
* Read for all devices
* Read for a single device
* Query for sets of devices
* Update devices
* Delete devices
* Schedule jobs
  * Execute methods
  * Update the device twin
* Get a list of jobs
* Get a single job

## Documentation

* View the API documentation in the [Wiki](https://github.com/Azure/iothub-manager-java/wiki)
* [Contributing and Development setup](CONTRIBUTING.md)
* [Development setup, scripts and tools](DEVELOPMENT.md)

# How to use

## Running the service with Docker

You can run the microservice and its dependencies using [Docker](https://www.docker.com/) with the instructions [here][run-with-docker-url].

## Running the service locally

## Prerequisites

### 1. Deploy Azure Services

This service has a dependency on the following Azure resources. Follow the instructions for [Deploy the Azure services](https://docs.microsoft.com/azure/iot-suite/iot-suite-remote-monitoring-deploy-local#deploy-the-azure-services).

* [Azure IoT Hub](https://docs.microsoft.com/azure/iot-hub/)

### 2. Setup Dependencies

This service has a dependency on the following Azure resources. Follow the instructions
for [Deploy the Azure services](https://docs.microsoft.com/azure/iot-suite/iot-suite-remote-monitoring-deploy-local#deploy-the-azure-services) to deploy the required resources.

* [Storage adapter microservice](https://github.com/Azure/remote-monitoring-services-java/tree/master/storage-adapter)

> Note: you can also use a [deployed endpoint][deploy-rm] with [Authentication disabled][disable-auth] (e.g. https://{your-resource-group}.azurewebsites.net/config/v1)

### 3. Environment variables required to run the service
In order to run the service, some environment variables need to be created at least once. See specific instructions for IDE or command line setup below for more information. More information on environment variables
[here](#configuration-and-environment-variables).
 
* `PCS_AAD_APPID` = { Azure service principal id }
* `PCS_AAD_APPSECRET` = { Azure service principal secret }
* `PCS_KEYVAULT_NAME` = { Name of Key Vault resource that stores settings and configuration }

### Configuration values used from Key Vault
Some of the configuration needed by the microservice is stored in an instance of Key Vault that was created on initial deployment. The iothub-manager microservice uses:

* `aadAppId` = Azure Active Directory application / service principal id.
* `authIssuer` = Identifies the security token service (STS) i.e. https://sts.windows.net/tenantId/ 
* `authRequired` = Whether or not authentication is needed for calls to microservices i.e. from the web ui or postman
* `authWebServiceUrl` = Endpoint for the remote monitoring auth microservice
* `corsWhitelist` = Specifies where requests are allowed from "{ 'origins': ['\*'], 'methods': ['\*'], 'headers': ['\*'] }" to allow everything. Empty to disable CORS
* `storageAdapterWebServiceUrl` = Endpoint for storage adapter microservice
* `subscriptionId` = GUID that uniquely identifies your subscription to use Azure services


# Running the service in an IDE

## Prerequisites
- Install [Intellij IDEA Community](https://www.jetbrains.com/idea/download)
- Install [SBT](http://www.scala-sbt.org/download.html)
- Install the latest [Java 8 SDK](http://www.oracle.com/technetwork/java/javase/downloads/index.html)

## Running the service with IntelliJ IDEA
Intellij IDEA lets you open the application without using a command
prompt, without configuring anything outside of the IDE. The SBT build tool
takes care of downloading appropriate libraries, resolving dependencies and
building the project (more info [here](https://www.playframework.com/documentation/2.6.x/IDE)).

Steps using IntelliJ IDEA Community 2017, with SBT plugin enabled:

* Make sure the [prerequisites](#prerequisites) are set up.
* "Open" the project with IntelliJ, the IDE should automatically recognize
  the SBT structure. Wait for the IDE to download some dependencies
  (see IntelliJ status bar). This may take a while, hang in there!
* Create a new Run Configuration, of type "SBT Task", with any name.
  * Enter "run 9002" (including the double quotes) in Tasks. This ensures that
   the service starts using the TCP port 9002.  If you desire to use a
    different port, feel free to change it.
  * Define the following environment variable:
    * `PCS_AAD_APPID` = { Azure service principal id }
    * `PCS_AAD_APPSECRET` = { Azure service principal secret }
    * `PCS_KEYVAULT_NAME` = { Name of Key Vault resource that stores settings and configuration }
* Either from the toolbar or the Run menu, execute the configuration just
  created, using the Debug command/button
* Test that the service is up and running pointing your browser to
  * http://127.0.0.1:9001/v1/status
  * http://127.0.0.1:9022/v1/status

## Running the service with Eclipse
The integration with Eclipse requires the [sbteclipse plugin](https://github.com/typesafehub/sbteclipse), already
included, and an initial setup via command line (more info
[here](https://www.playframework.com/documentation/2.6.x/IDE)).

Steps using Eclipse Oxygen ("Eclipse for Java Developers" package):

* Open a console (either Bash or Windows CMD), move into the project folder,
  execute `sbt compile` and then `sbt eclipse`. This generates some files
  required by Eclipse to recognize the project.
* Open Eclipse, and from the Welcome screen "Import" an existing project,
  navigating to the root folder of the project.
* From the console run `sbt -jvm-debug 9999 "run 9002"` to start the project
* Test that the service is up and running pointing your browser to
  http://127.0.0.1:9002/v1/status
* In Eclipse, select "Run -> Debug Configurations" and add a "Remote Java
  Application", using "localhost" and port "9999".
* After saving this configuration, you can click "Debug" to connect to the
  running application.

# Running the service from the command line

1. Make sure the [prerequisites](#prerequisites) are set up.
1. Set the following environment variables in your system. More information on environment variables [here](#configuration-and-environment-variables).
    * `PCS_AAD_APPID` = { Azure service principal id }
    * `PCS_AAD_APPSECRET` = { Azure service principal secret }
    * `PCS_KEYVAULT_NAME` = { Name of Key Vault resource that stores settings and configuration }
1. Use the scripts in the [scripts](scripts) folder for many frequent tasks:

* `build`: compile all the projects and run the tests.
* `compile`: compile all the projects.
* `run`: compile the projects and run the service. This will prompt for
  elevated privileges in Windows to run the web service.

If you are familiar with [SBT](http://www.scala-sbt.org), you can also use SBT
directly. A copy of SBT is included in the root of the project.

## Project Structure

* **Code** for the application is in **app/com/microsoft/azure/iotsolutions/iothubmanager**

  * **WebService** - Web service exposing REST interface for Iot Hub
    communication.
  * **Services** - Business logic for interacting with IoTHub

* **Test** - Unit tests for the application are in this folder
  * **WebService** - Tests for web service functionality
  * **Service** - Tests for service functionality
* **Conf** - Contains configuration files and routes are in the **conf** folder
* **Scripts** - This folder contains build scripts, docker container creation scripts,
   and scripts for running the microservice from the command line

## Updating the Docker image

The `scripts` folder includes a [docker](scripts/docker) subfolder with the files
required to package the service into a Docker image:

* `build`: build a Docker container and store the image in the local registry.
* `run`: run the Docker container from the image stored in the local registry.

You might notice that there is no `Dockerfile`. All Docker settings are
defined in [build.sbt](build.sbt).

```scala
dockerRepository := Some("azureiotpcs")
dockerAlias := DockerAlias(dockerRepository.value, None, packageName.value + "-java", Some((version in Docker).value))
dockerBaseImage := "toketi/openjdk-8-jre-alpine-bash"
dockerUpdateLatest := false
dockerBuildOptions ++= Seq("--compress", "--label", "Tags=azure,iot,pcs,telemetry,Java")
dockerEntrypoint := Seq("bin/telemetry")
```

The package logic is executed via
[sbt-native-packager](https://github.com/sbt/sbt-native-packager), installed
in [plugins.sbt](project/plugins.sbt).

# Configuration and Environment variables

The service configuration is stored using Akka's
[HOCON](https://github.com/typesafehub/config/blob/master/HOCON.md)
format in [application.conf](conf/application.conf).

The HOCON format is a human readable format, very close to JSON, with some
useful features:

* Ability to write comments
* Support for substitutions, e.g. referencing environment variables
* Supports JSON notation

The configuration file in the microservice references some environment
variables that need to created at least once. Depending on your OS and
the IDE, there are typically set in 3 different ways:
* Environment variables as is the case with ${PCS_AAD_APPID}. This is typically only done with the 3 variables described above as these are needed to access Key Vault. More details about setting environment variables are located below.
* Key Vault: A number of the settings in this file will be blank as they are  expecting to get their value from a Key Vault secret of the same name.
* Direct Value: For some values that aren't typically changed or for local development you can set the value directly in the file.
* IntelliJ IDEA: env. vars, esp. key-vault related, can be set in each Run Configuration, see
  https://www.jetbrains.com/help/idea/run-debug-configuration-application.html

To save the environment variables globally and make them persistent, please follow these pages:
* https://superuser.com/questions/949560/
* https://stackoverflow.com/questions/13046624/how-to-permanently-export-a-variable-in-linux
* https://stackoverflow.com/questions/135688/setting-environment-variables-in-os-x
* https://help.ubuntu.com/community/EnvironmentVariables

# Contributing to the solution

Please follow our [contribution guidelines](CONTRIBUTING.md).  We love PRs too.

# Feedback

Please enter issues, bugs, or suggestions as GitHub Issues here: https://github.com/Azure/iot-hub-manager-java/issues.

# License

Copyright (c) Microsoft Corporation. All rights reserved.
Licensed under the [MIT](LICENSE) License.

[build-badge]: https://img.shields.io/travis/Azure/iothub-manager-java.svg
[build-url]: https://travis-ci.org/Azure/iothub-manager-java
[issues-badge]: https://img.shields.io/github/issues/azure/iothub-manager-java.svg
[issues-url]: https://github.com/azure/iothub-manager-java/issues
[gitter-badge]: https://img.shields.io/gitter/room/azure/iot-solutions.js.svg
[gitter-url]: https://gitter.im/azure/iot-solutions

[project-wiki]: https://github.com/Azure/iot-hub-manager-java/wiki/%5BAPI-Specifications%5D-Messages
[run-with-docker-url]:(https://docs.microsoft.com/azure/iot-suite/iot-suite-remote-monitoring-deploy-local#run-the-microservices-in-docker)
[rm-arch-url]:https://docs.microsoft.com/en-us/azure/iot-suite/iot-suite-remote-monitoring-sample-walkthrough
[postman-url]: https://www.getpostman.com
[windows-envvars-howto-url]: https://superuser.com/questions/949560/how-do-i-set-system-environment-variables-in-windows-10
[deploy-rm]:https://docs.microsoft.com/azure/iot-suite/iot-suite-remote-monitoring-deploy
[disable-auth]:https://github.com/Azure/azure-iot-pcs-remote-monitoring-dotnet/wiki/Developer-Reference-Guide#disable-authentication
[iothub-connstring-blog]:https://blogs.msdn.microsoft.com/iotdev/2017/05/09/understand-different-connection-strings-in-azure-iot-hub/