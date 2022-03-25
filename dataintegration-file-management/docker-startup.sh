#!/bin/sh

# set -x就可有详细的日志输出.
set -x

#===========================================================================================
# JVM Configuration
#===========================================================================================
JAVA_OPT="-server -Xms${JVM_XMS} -Xmx${JVM_XMX} -XX:+PrintGCDetails -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./ -Xloggc:./gc.log"
JAVA_OPT="${JAVA_OPT} -Duser.timezone=${TIME_ZONE} -Dfile.encoding=${ENCODING} ${JVM_CUSTOM}"

if [[ "${JVM_DEBUG}" == "y" ]]; then
  JAVA_OPT="${JAVA_OPT} -Xdebug -Xrunjdwp:transport=dt_socket,address=${JVM_DEBUG_PORT},server=y,suspend=n"
fi

echo "java is starting,you can check the"
echo "java ${JAVA_OPT} -jar ${JAR_FILE}"
java ${JAVA_OPT} -jar ${JAR_FILE}
