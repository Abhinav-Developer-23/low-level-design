@echo off
echo Compiling LinkedIn Platform...

if not exist "target\classes" mkdir "target\classes"

:: Compile enums
javac -d target/classes src/main/java/org/example/enums/*.java

:: Compile model classes
javac -cp target/classes -d target/classes src/main/java/org/example/model/*.java

:: Compile interfaces
javac -cp target/classes -d target/classes src/main/java/org/example/interfaces/*.java

:: Compile services
javac -cp target/classes -d target/classes src/main/java/org/example/services/*.java

:: Compile strategies
javac -cp target/classes -d target/classes src/main/java/org/example/strategies/*.java

:: Compile observers
javac -cp target/classes -d target/classes src/main/java/org/example/observers/*.java

:: Compile system
javac -cp target/classes -d target/classes src/main/java/org/example/system/*.java

:: Compile Main
javac -cp target/classes -d target/classes src/main/java/org/example/Main.java

echo.
echo Compilation complete!
echo.
echo Running LinkedIn Demo...
echo ========================================
echo.

java -cp target/classes org.example.Main

echo.
echo ========================================
echo Demo completed!
pause

