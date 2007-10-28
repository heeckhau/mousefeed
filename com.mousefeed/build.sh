#!/bin/bash
# runs build

CP="lib/ant-1.7.0.jar:lib/ant-launcher-1.7.0.jar:lib/ant-junit-1.7.0.jar"
java -cp $CP org.apache.tools.ant.launch.Launcher -f build1.xml $@ 
     