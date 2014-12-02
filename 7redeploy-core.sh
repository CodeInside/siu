# set JAVA_HOME
#export JAVA_HOME=/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Home
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.7.0_67.jdk/Contents/Home

# redeploy gws-client-midrf-3894
rm /Users/dmitryermolaev/work/servers/glassfish3/glassfish/domains/domain1/autodeploy/bundles/gws-core-1.0.7.jar

mvn -f gws-core/pom.xml -DskipTests clean install package

cp gws-core/target/gws-core-1.0.7.jar /Users/dmitryermolaev/work/servers/glassfish3/glassfish/domains/domain1/autodeploy/bundles


# show log
./tail_log.sh