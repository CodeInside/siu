del /Q d:\work\glassfish3\glassfish\domains\domain1\autodeploy\bundles\

call mvn -f pom.xml -DskipTests clean install

call mvn -f uat-deployer\pom.xml -DskipTests clean package

xcopy /Q d:\work\siu\uat-deployer\target\bundles\* d:\work\glassfish3\glassfish\domains\domain1\autodeploy\bundles\