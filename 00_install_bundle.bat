del /Q c:\work\glassfish3\glassfish\domains\domain1\autodeploy\bundles\gws-s-oep-declarer-1.0.5.jar

call mvn -f gws-s-oep-declarer\pom.xml -DskipTests clean install

call mvn -f uat-deployer\pom.xml -DskipTests clean package

xcopy /Q c:\work\siu\uat-deployer\target\bundles\gws-s-oep-declarer-1.0.5.jar c:\work\glassfish3\glassfish\domains\domain1\autodeploy\bundles\