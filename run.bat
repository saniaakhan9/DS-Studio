@echo off
setlocal
set "JAVA_HOME=C:\tools\jdk-21.0.6+7"
set "PATH=%JAVA_HOME%\bin;C:\tools\apache-maven-3.9.9\bin;%PATH%"

set "MODULE_PATH=C:\Users\Sania\.m2\repository\org\openjfx\javafx-base\21.0.6\javafx-base-21.0.6-win.jar;C:\Users\Sania\.m2\repository\org\openjfx\javafx-controls\21.0.6\javafx-controls-21.0.6-win.jar;C:\Users\Sania\.m2\repository\org\openjfx\javafx-fxml\21.0.6\javafx-fxml-21.0.6-win.jar;C:\Users\Sania\.m2\repository\org\openjfx\javafx-graphics\21.0.6\javafx-graphics-21.0.6-win.jar"
set "CP=target\classes;C:\Users\Sania\.m2\repository\org\xerial\sqlite-jdbc\3.45.2.0\sqlite-jdbc-3.45.2.0.jar;C:\Users\Sania\.m2\repository\org\slf4j\slf4j-api\1.7.36\slf4j-api-1.7.36.jar"

echo ========================================================
echo   Starting DS Studio (Interactive Data Structures Lab)
echo ========================================================

java --module-path "%MODULE_PATH%" --add-modules javafx.controls,javafx.fxml -cp "%CP%" com.dsstudio.DSStudioApp
pause
