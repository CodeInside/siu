# set JAVA_HOME
export JAVA_HOME=/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Home
#export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.7.0_67.jdk/Contents/Home

# stop server
./stop_server.sh

# remove all old files
rm /Users/dmitryermolaev/work/servers/glassfish3/glassfish/domains/domain1/autodeploy/bundles/*

# build
# mvn -DskipTests=true clean install
mvn -DskipTests=true clean install

# package
mvn -f uat-deployer/pom.xml clean package

# deploy
cp uat-deployer/target/bundles/* /Users/dmitryermolaev/work/servers/glassfish3/glassfish/domains/domain1/autodeploy/bundles

# clear logs
./clear_logs.sh

# start server
# ./start_server.sh


# open log
# subl /Users/dmitryermolaev/work/servers/glassfish3/glassfish/domains/domain1/logs/server.log
