#!/bin/bash
# runs build

CP="$JAVA_HOME/lib/tools.jar:lib/ant-1.7.0.jar:lib/ant-launcher-1.7.0.jar:lib/ant-junit-1.7.0.jar"
$JAVA_HOME/bin/java -cp $CP org.apache.tools.ant.launch.Launcher -f build1.xml $@ 
     