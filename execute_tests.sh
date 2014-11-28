#export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.7.0_67.jdk/Contents/Home
export JAVA_HOME=/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Home
export MAVEN_OPTS="-XX:ErrorFile=/Users/dmitryermolaev/Downloads/1 -Xmx2048m -XX:MaxPermSize=128m" 

mvn clean install