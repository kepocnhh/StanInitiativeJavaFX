# StanInitiativeJavaFX
StanInitiative project on JavaFX

# Windows build information
## build
> "%JAVA_HOME%/bin/javac" -sourcepath ./src/main/java -d bin -classpath "%JAVA_HOME%/jre/lib/jfxrt.jar";lib/* ./src/main/java/stan/initiative/Main.java

## build_css
> "%JAVA_HOME%/bin/javafxpackager" -createbss -srcdir ./src/main/css -outdir bin/css -srcfiles StanTheme.css -v

## copy_res
> xcopy .\src\main\res .\bin\res /E

## run
> "%JAVA_HOME%/bin/java" -classpath "%JAVA_HOME%/jre/lib/jfxrt.jar";lib/*;bin stan.initiative.Main

## build_jar
> "%JAVA_HOME%/bin/javafxpackager" -createjar -appclass stan.initiative.Main -srcdir bin -classPath "lib/StanVoiceRecognition.jar" -outfile StanInitiative

## run_jar
> "%JAVA_HOME%/bin/javaw" -jar StanInitiative.jar

# Configuration description
```json
{
  "telegram": {},
  "google": {},
  "media": {},
  "commander": {}
}
```
## telegram
```json
{
  "bot": {
    "id": 123456789,
    "token": "myawesometelegrambottokenqwertyuiop",
    "chatIdMe": 12345678
  }
}
```

## google
```json
{
  "speechapi": {
    "apikey": "qwertyuiop123-qwertyuiop123456789010111"
  }
}
```

## media
```json
{
  "music": {
    "mainFolder": "C:/Users/username/Music"
  },
  "images": {
    "screenShotFolder": "E:/Downloads"
  }
}
```

## commander
```json
{
  "modes": [],
  "states": [],
  "commands": []
}
```
> mode
```json
{
  "name": "mode1",
  "states": []
}
```
> state
```json
{
  "name": "state1",
  "commands": []
}
```
> command
```json
{
  "name": "command1",
  "keys": [
    "key1"
  ]
}
```