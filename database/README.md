Artifact for generating DB schema with Liquibase
================================================
* Build it: <code>mvn clean install</code>

Generating schema
-----------------------------------
* <code>cd target</code>
* Generate schema: <code>java -jar database-*-SNAPSHOT.jar update</code>
* Drop schema: <code>java -jar database-*-SNAPSHOT.jar dropAll</code>
* See other options: <code>java -jar database-*-SNAPSHOT.jar</code>
