del /Q d:\work\glassfish3\glassfish\domains\domain1\autodeploy\bundles\gws-s-oep-declarer-1.0.6.jar

call mvn -f gws-s-oep-declarer\pom.xml -DskipTests clean install

call mvn -f uat-deployer\pom.xml -DskipTests clean package

xcopy /Q d:\work\siu\uat-deployer\target\bundles\gws-s-oep-declarer-1.0.6.jar d:\work\glassfish3\glassfish\domains\domain1\autodeploy\bundles\