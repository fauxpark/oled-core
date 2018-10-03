#!/bin/sh
echo "assume classes are already build (with f.e. maven in target/classes)"

REPO=~/.m2/repository

SLF4J_API=$REPO/org/slf4j/slf4j-api/1.7.25/slf4j-api-1.7.25.jar
SLF4J_SIMPLE=$REPO/org/slf4j/slf4j-simple/1.7.25/slf4j-simple-1.7.25.jar
PI4J=$REPO/com/pi4j/pi4j-core/1.2-SNAPSHOT/pi4j-core-1.2-SNAPSHOT.jar

CP=target/classes:$SLF4J_API:$SLF4J_SIMPLE:$PI4J
java -cp $CP  net.fauxpark.oled.Main $@
