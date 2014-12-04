# set JAVA_HOME
export JAVA_HOME=/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Home
#export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.7.0_67.jdk/Contents/Home

#cp uat-deployer/target/bundles/* /Users/dmitryermolaev/work/servers/glassfish3/glassfish/domains/domain1/autodeploy/bundles

./clear_logs.sh ; ./start_server.sh ; ./tail_log.sh