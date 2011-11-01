Domain and DAO's
================
* Build it: <code>mvn clean install</code>

Import testdata for the integration tests
-----------------------------------
* <code>mvn sql:execute</code>

Skipping targets:
-----------------
The following options are available to skip certain targets:

* <code>-DskipTests</code> (skip all tests)
* <code>-Dintegration=false</code> (skip all integration tests)
