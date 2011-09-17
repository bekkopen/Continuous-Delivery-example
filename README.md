Example Java webapp infrastructure for Continuous Delivery 
============================================================
This is an example of a Java webapp project which supports techniques for continuous delivery of software into production.

Core concepts are:

* Embedded Jetty webapp for lightweight configuration, ease of use and single self contained deployment of a single binary to any environment.
* Scripted deployment of the app which supports both push and pull deployment.

Content:
--------
* pom.xml: Parent pom for the entire project.
* database/: Database artifact. Contains code for interacting with the database and generating schemas.
* webapp/: Embedded Jetty webapp artifact. Contains the deployable webapp artifact.
* scripts/: Contains scripts for deploying, starting and monitoring of the webapp.

Preconditions:
--------------
* CI environment (e. g. Jenkins)
* Artifact repository (e. g. Nexus) 

Usage:
------
* Build it: <code>mvn clean install</code>
* See Readme.md in sub projects for usage of the different artifacts.
