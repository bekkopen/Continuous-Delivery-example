Sett opp utviklingsmiljø:
-------------------------
- Git, Ruby, VirtualBox, Vagrant, Java, Maven, IDE
 
Sett opp produksjonslikt lokalt utviklingsmiljø:
------------------------------------------------
- Starte opp vagrant og ssh-e seg inn
- Lage en puppet-klasse som legger en fil på server
- Lage/finne egen public-key og provisjonere denne inn på serverne
- Lage en puppet-modul som installerer en apt-pakke
- Sette opp vagrant til å dele en villkårlig mappe 

Test appen på produksjonslikt milø:
------------------------------------
- Ssh inn på vagrant-boks
- Kjør mvn exec:java
- Push deploy med Maven: mvn clean install -Dpush-deploy (-Dssh-port=2200)

Versjonering av database med Liquibase:
------------------------------------
- Opprett skjema i lokal MySql
- Populer skjema med testdata (mvn sql:execute)

Push deploy til test-server:
-------------------------
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
