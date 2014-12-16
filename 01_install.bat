del /Q c:\work\glassfish3\glassfish\domains\domain1\autodeploy\bundles\

rem call mvn -f pom.xml -DskipTests clean install

rem call mvn -f gws-api\pom.xml -DskipTests clean install

rem call mvn -f gws-core\pom.xml -DskipTests clean install

call mvn -f web-client\pom.xml -DskipTests clean install

rem call mvn -f gws-client-fns-3626\pom.xml -DskipTests clean install 

call mvn -f uat-deployer\pom.xml -DskipTests clean package

xcopy /Q c:\work\siu\uat-deployer\target\bundles\* c:\work\glassfish3\glassfish\domains\domain1\autodeploy\bundles\