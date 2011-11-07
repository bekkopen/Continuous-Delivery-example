Artifact for generating DB schema with Liquibase
================================================
* Build it: <code>mvn clean install</code>

Generating schema
-----------------------------------
* <code>cd target</code>
* Generate schema: <code>java -jar database-*-SNAPSHOT.jar update</code>
* Drop schema: <code>java -jar database-*-SNAPSHOT.jar dropAll</code>
* See other options: <code>java -jar database-*-SNAPSHOT.jar</code>

Preparing the database
-----------------------
* Comment out <code>#bind-address = 127.0.0.1</code> in <code>/etc/mysql/my.cnf</code> and restart the mysql service (<code>service mysql restart</code>).
* <code>CREATE USER 'bekkopen'@'localhost' IDENTIFIED BY 'hemmelig';</code>
* <code>GRANT ALL PRIVILEGES ON \*.\* TO 'bekkopen'@'localhost' IDENTIFIED BY 'hemmelig' WITH GRANT OPTION;</code>
* <code>CREATE USER 'bekkopen'@'%' IDENTIFIED BY 'hemmelig';</code>
* <code>GRANT ALL PRIVILEGES ON \*.\* TO 'bekkopen'@'%' IDENTIFIED BY 'hemmelig' WITH GRANT OPTION;</code>
