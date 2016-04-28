# StanInitiativeJavaFX
StanInitiative project on JavaFX

# Windows build information
## build
"%JAVA_HOME%/bin/javac" -sourcepath ./src -d bin -classpath "%JAVA_HOME%/jre/lib/jfxrt.jar" ./src/main/java/stan/initiative/Main.java
## run
"%JAVA_HOME%/bin/java" -classpath "%JAVA_HOME%/jre/lib/jfxrt.jar";bin stan.initiative.Main