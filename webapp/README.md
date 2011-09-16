Usage
=====

* Build it: <code>mvn clean install</code>
* Unzip it: <code>cd webapp/target; unzip webapp-*.zip</code>
* Get configuration: <code>cp ../../config/webapp.properties .</code>
* Edit webapp.properties in your favourite editor (remember to set hostname to your hostname).
* Run it: <code>cd webapp-*/bin; ./webapp</code>

Test usage
===========
* Set your hostname: <code>export HOSTNAME=$(hostname)</code>. If you are on Unix/Linux/Mac put it in your <code>~/.bash_profile</code>. If you are on Windows, make your hostname an environment variable.
* Build it: <code>mvn clean install</code>
* Run it: <code>mvn exec:java</code>

Jetty Run
=========
* Allthough discouraged <code>mvn jetty:run</code>
