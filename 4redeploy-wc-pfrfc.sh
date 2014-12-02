# set JAVA_HOME
#export JAVA_HOME=/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Home
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.7.0_67.jdk/Contents/Home

# redeploy web-client
rm /Users/dmitryermolaev/work/servers/glassfish3/glassfish/domains/domain1/autodeploy/bundles/web-client-1.0.9.jar

mvn -f web-client/pom.xml -DskipTests clean install package

cp web-client/target/web-client-1.0.9.jar /Users/dmitryermolaev/work/servers/glassfish3/glassfish/domains/domain1/autodeploy/bundles


# redeploy gws-client-pfrf-3814
rm /Users/dmitryermolaev/work/servers/glassfish3/glassfish/domains/domain1/autodeploy/bundles/gws-client-pfrf-3814-1.0-SNAPSHOT.jar

mvn -f gws-client-pfrf-3814/pom.xml -DskipTests clean install package

cp gws-client-pfrf-3814/target/gws-client-pfrf-3814-1.0-SNAPSHOT.jar /Users/dmitryermolaev/work/servers/glassfish3/glassfish/domains/domain1/autodeploy/bundles


# show log
./tail_log.sh