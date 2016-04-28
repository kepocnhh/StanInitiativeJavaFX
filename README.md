# StanInitiativeJavaFX
StanInitiative project on JavaFX

# Windows build information
## build
> "%JAVA_HOME%/bin/javac" -sourcepath ./src/main/java -d bin -classpath "%JAVA_HOME%/jre/lib/jfxrt.jar";lib/* ./src/main/java/stan/initiative/Main.java

## run
> "%JAVA_HOME%/bin/java" -classpath "%JAVA_HOME%/jre/lib/jfxrt.jar";lib/*;bin stan.initiative.Main

## build_jar
> "%JAVA_HOME%/bin/javafxpackager" -createjar -appclass stan.initiative.Main -srcdir bin -classPath "lib/StanVoiceRecognition.jar" -outfile StanInitiative

## run_jar
> "%JAVA_HOME%/bin/javaw" -jar StanInitiative.jar