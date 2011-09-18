Usage
=====

Alternative 1:
--------------
* Build it: <code>mvn clean install</code>
* Unzip it: <code>cd webapp/target; unzip webapp-*.zip</code>
* Run it: <code>cd webapp-*/bin; ./webapp</code>

Alternative 2:
--------------
* Build it: <code>mvn clean install</code>
* Run it: <code>mvn exec:java</code>

Not an alternative:
-------------------
* We don't do jetty:run. Use <code>mvn exec:java</code> instead. We want to run the same code every time!

Deployment
==========

Alternative 1:
--------------
* Use ../scripts/push_deploy.sh

Alternative 2:
--------------
# <code>mvn clean install -Dpush-deploy</code>
# <code>mvn clean -Dpull-deploy</code>
# <code>mvn clean -Drollback</code>
