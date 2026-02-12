@echo off
REM Event Ticket Booking System - Build Script for Windows
REM This script compiles and runs the Java application

echo ===================================
echo Event Ticket Booking System
echo Build Script
echo ===================================
echo.

REM Create bin directory if it doesn't exist
if not exist "bin" (
    echo Creating bin directory...
    mkdir bin
)

REM Clean previous build
echo Cleaning previous build...
if exist "bin\*" del /Q bin\*

REM Compile Java files
echo Compiling Java files...
dir /s /B src\main\java\*.java > sources.txt

javac -d bin @sources.txt

if %ERRORLEVEL% EQU 0 (
    echo [OK] Compilation successful!
    echo.
    
    REM Run the application
    echo Running application...
    echo ===================================
    echo.
    java -cp bin com.eventbooking.BookingSystemApplication
    
    echo.
    echo ===================================
    echo [OK] Execution completed!
) else (
    echo [ERROR] Compilation failed!
    exit /b 1
)

REM Clean up
del sources.txt

pause
