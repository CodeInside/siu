# set JAVA_HOME
#export JAVA_HOME=/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Home
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.7.0_67.jdk/Contents/Home

rm /Users/dmitryermolaev/work/servers/glassfish3/glassfish/domains/domain1/autodeploy/bundles/gws-client-rr-3564-1.0.0.jar

mvn -f gws-client-rr-3564/pom.xml -DskipTests clean install package

cp gws-client-rr-3564/target/gws-client-rr-3564-1.0.0.jar /Users/dmitryermolaev/work/servers/glassfish3/glassfish/domains/domain1/autodeploy/bundles

./tail_log.sh
