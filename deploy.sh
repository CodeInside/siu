# stop server
/Users/dmitryermolaev/work/servers/glassfish3/bin/asadmin stop-domain

# remove all old files
rm /Users/dmitryermolaev/work/servers/glassfish3/glassfish/domains/domain1/autodeploy/bundles/*

# build
mvn -DskipTests=true install

# package
mvn -f uat-deployer/pom.xml package

# deploy
cp uat-deployer/target/bundles/* /Users/dmitryermolaev/work/servers/glassfish3/glassfish/domains/domain1/autodeploy/bundles
