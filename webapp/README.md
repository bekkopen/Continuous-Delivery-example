Usage
=====

* Build it: <code>mvn clean install</code>
* Unzip it: <code>cd webapp/target; unzip webapp-*.zip</code>
* Get configuration: <code>cp ../../config/webapp.properties .</code>
* Edit webapp.properties in your favorite editor (remember to set hostname to your hostname).
* Run it: <code>cd webapp-*/bin; ./webapp</code>

Test usage
===========
* Build it: <code>mvn clean install</code>
* Run it: <code>mvn exec:java</code>

Jetty Run
=========
* Although discouraged: <code>mvn jetty:run</code>
