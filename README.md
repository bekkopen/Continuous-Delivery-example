Example Java webapp infrastructure for Continuous Delivery 
============================================================
This is an example of a Java webapp project which supports techniques for continuous delivery.

Core concepts are:

* Embedded Jetty webapp for lightweight configuration, ease of use and single self contained deployment of a single binary to any environment.
* Scripted deployment of the app which supports both push and pull deployment.

Content:
--------
* settings.xml.example: Example settings.xml. Rename and put in ~/.m2.
* pom.xml: Parent pom for the entire project.
* database/: Contains code for generating DB schema with Liquibase.
* core/: Domain objects and Dao's.
* webapp/: Embedded Jetty webapp artifact. Contains the deployable webapp artifact.
* scripts/: Contains scripts for deploying, starting and monitoring of the webapp.
* config/: Contains config for the app and the deploy script.

Preconditions:
--------------
* CI environment (e. g. Jenkins)
* Artifact repository (e. g. Nexus)
* Database (e. g. MySQL)

Usage:
------
* Build it: <code>mvn clean install</code>
* See Readme.md in sub projects for usage of the different artifacts.

Releasing:
----------
* <code>mvn -B release:prepare -Dintegration=false</deploy>
* <code>mvn release:perform -DconnectionUrl=scm:git:git@github.com:bekkopen/Continuous-Delivery-example.git -Dintegration=false -Dgoals=deploy</code>
* <code>mvn clean deploy -Dintegration=false</code>
