# set JAVA_HOME
export JAVA_HOME=/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Home
#export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.7.0_67.jdk/Contents/Home

# stop server
./stop_server.sh

# remove all old files
rm /Users/dmitryermolaev/work/servers/glassfish3/glassfish/domains/domain1/autodeploy/bundles/*

# build web-client
mvn -f web-client/pom.xml -DskipTests clean install

#mvn -f gws-core/pom.xml -DskipTests clean install

# package
mvn -f uat-deployer/pom.xml clean package

# deploy
cp uat-deployer/target/bundles/* /Users/dmitryermolaev/work/servers/glassfish3/glassfish/domains/domain1/autodeploy/bundles


