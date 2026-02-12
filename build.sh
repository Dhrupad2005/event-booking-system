#!/bin/bash

# Event Ticket Booking System - Build Script
# This script compiles and runs the Java application

echo "==================================="
echo "Event Ticket Booking System"
echo "Build Script"
echo "==================================="
echo ""

# Create bin directory if it doesn't exist
if [ ! -d "bin" ]; then
    echo "Creating bin directory..."
    mkdir -p bin
fi

# Clean previous build
echo "Cleaning previous build..."
rm -rf bin/*

# Compile Java files
echo "Compiling Java files..."
find src/main/java -name "*.java" > sources.txt

javac -d bin @sources.txt

if [ $? -eq 0 ]; then
    echo "✓ Compilation successful!"
    echo ""
    
    # Run the application
    echo "Running application..."
    echo "==================================="
    echo ""
    java -cp bin com.eventbooking.BookingSystemApplication
    
    echo ""
    echo "==================================="
    echo "✓ Execution completed!"
else
    echo "✗ Compilation failed!"
    exit 1
fi

# Clean up
rm sources.txt
