Usage
=====

* Add your war file artifact to the Maven <code>pom.xml</code>
* Build it from parent: <code>cd ..; mvn clean install</code>
* Unzip it: <code>cd jetty-pkg/target; unzip jetty-pkg-*.zip</code>
* Get configuration: <code>cp ../../config/webapp.properties .</code>
* Edit webapp.properties in your favourite editor (remember to set hostname to your hostname).
* Run it: <code>cd jetty-pkg-*/bin; ./jetty-pkg</code>
