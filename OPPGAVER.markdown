Sett opp utviklingsmiljø:
-------------------------
- Git, Ruby, VirtualBox (v. 4.1.4, virtualbox.org), Vagrant, Java, Maven, IDE
  - gem install vagrant --no-rdoc --no-ri
  
- Deltakerne får utlevert memory stick med VirtualBox og ubuntu-baseboks
- Sjekk ut prosjektet:
  - git clone https://github.com/bekkopen/Continuous-Delivery-example
  - cd Continuous-Delivery-example
  - git submodule init
  - git submodule update
- Installer ubuntu-base box (stå i minnepinne-mappa)
  - vagrant box add oneiric32 oneiric32.box
- Start opp db- og web-server
  - vagrant up
- Ny terminal
  - mvn clean install
  - cd webapp
  - mvn process-test-resources exec:java -Djetty.port=9191 -Ddev=true -Drun.exploded=true
  - åpne browser på localhost:9191
 
Sett opp produksjonslikt lokalt utviklingsmiljø:
------------------------------------------------
- Starte opp vagrant og ssh inn
  - vagrant ssh web
  - vagrant ssh db
  - observere hvilken bruker man er logget inn med
  - bytte bruker til 'bekkopen'-bruker (sudo su bekkopen)
  - eller logge seg inn 'ssh bekkopen@localhost -p 2200'
- Lage/finne egen public-key og provisjonere denne inn på bekkopen-bruker
    * hvis du ikke har egen privat/public-key, stå i ~/.ssh og kjør 'ssh-keygen'
    * eksempel på kode i puppet/manifests/classes/users.pp  
    * ssh bekkopen@localhost -p 2200
- Provisjonere maven på web-noden
  * eksempel i puppet/modules/nexus
  * husk symlinking av mvn-kommandoen
- Dele ~/.m2-mappe med web-boksen.
  - i /home/vagrant


Test appen på produksjonslikt milø:
------------------------------------
- Ssh inn på vagrant-boks (som vagrant)
- Kjør mvn exec:java
- (Push deploy med Maven: mvn clean install -Dpush-deploy (-Dssh-port=2200))

Konfigurer app v.h.a secret.properties
--------------------------------------
TODO

Versjonering av database med Liquibase:
------------------------------------
- Opprett skjema i lokal MySql 
   * cd database/
   * modifiser src/main/java/no/bekk/bekkopen/cde/database/Main.java
   * mvn clean install
   * java -jar database-*-SNAPSHOT.jar update
- Populer skjema med testdata (mvn sql:execute)
   * mvn process-test-resources sql:execute -Ddb.host=localhost

Push deploy til test-server:
-------------------------
- 
- Push deploy med Maven: mvn clean install -Dpush-deploy -Dhost=<hostname>
- Push deploy med skript: ./remote_deploy.sh <node>

- Push deploy fra Jenkins

Pull deploy (single binary)  til QA/Prod:
-----------------------------------------
- Bygg og deploy en releasekandidat til Nexus
- Pull deploy fra produksjon/qa: ./deploy.sh webapp <version>
- Rull tilbake til forrige versjon ./deploy.sh webapp <version>.backup

Konfigurasjonsstyring og feature toggling:
------------------------------------------
- Lag en feature toggle for det du har utviklet.
- Forstå hvordan properties og toggles funker

Overvåkning:
------------
- Lag enkel overvåkning av at serveren er oppe med skript og cron.

Git – featurebranching
-----------------------
- Utvid modellen med scope for artefakten.

... flere paralelle oppgaver.

Integrasjonstesting / Trege tester – Jenkins
--------------------------------------------
- Lag en integrasjonstest
- Lag en slowtest.


Ressurser
--------------------------------------------
- Bygge eget vagrant-image: http://www.yodi.me/blog/2011/10/26/build-base-box-vagrant-ubuntu-oneiric-11.10-server/
- Installere puppetmaster på prod-server: http://www.ctrip.ufl.edu/install-puppetmaster-puppets-debian-lenny
- Eksempel på mysql-puppet-modul: http://bitfieldconsulting.com/puppet-and-mysql-create-databases-and-users
- Teste syntax på puppet i jenkins: http://mig5.net/node/341
