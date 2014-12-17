del /Q c:\work\glassfish3\glassfish\domains\domain1\autodeploy\bundles\

call mvn -f pom.xml -DskipTests clean install

call mvn -f uat-deployer\pom.xml -DskipTests clean package

xcopy /Q c:\work\siu\uat-deployer\target\bundles\* c:\work\glassfish3\glassfish\domains\domain1\autodeploy\bundles\