Denne filen beskriver hvordan man setter opp miljøet 
-----------------------------------------------------
** OBS! Disse instruksjonene gjelder for linux og mac, windows brukes på eget ansvar **


1. Installer siste versjon av virtualbox fra http://virtualbox.org
2. Ruby 1.9.2
 - Usikker på om du har ruby 1.9.2, kjør kommandoen
  ruby -v
  
 - Ruby kan installeres vha. rvm https://rvm.beginrescueend.com/rvm/install/ eller rbenv: https://github.com/sstephenson/rbenv#section_2
3. Vagrant:
  - gem install vagrant

4. Maven 3: http://maven.apache.org/download.html


For å verifisere at alt er riktig satt opp kan du gjøre følgende:
-----------------------------------------------------------------

1. Sjekk ut prosjektet:
  - git clone https://github.com/bekkopen/Continuous-Delivery-example
  - cd Continuous-Delivery-example
  - git submodule init
  - git submodule update
  
  
2. Start vagrant (dette tar litt tid i og med at det skal lastes ned et vm-image):
  - vagrant up
  
3 .Kopier maven settings.xml fra <prosjektmappe>/config/settings.xml.example til ~/.m2/settings.xml (ta backup av evt. eksisterende settings.xml)  

4. Kjør maven-bygg
  - mvn clean install
  - cd webapp
  - mvn process-test-resources exec:java -Djetty.port=9191 -Ddev=true -Drun.exploded=true
  - åpne browser på localhost:9191
  
  
5. BE READY FOR BUCKETS OF AWESOME!


