@echo off

echo assume classes are already build (with f.e. maven in target/classes)

set slf4j=%userprofile%/.m2/repository/org/slf4j/slf4j-api/1.7.25/slf4j-api-1.7.25.jar;%userprofile%/.m2/repository/org/slf4j/slf4j-simple/1.7.25/slf4j-simple-1.7.25.jar
set pi4j=%userprofile%/.m2/repository/com/pi4j/pi4j-core/1.2-SNAPSHOT/pi4j-core-1.2-SNAPSHOT.jar

java -cp target/classes;%slf4j%;%pi4j% net.fauxpark.oled.Main %*
