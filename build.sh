#!/bin/bash

# Build script for TP2 - Publisher-Subscriber System
# Compiles Java source files and creates executable JAR

echo "=== Building TP2 Project ==="

# Clean old compiled files
if [ -d "out" ]; then
    echo "Cleaning out directory..."
    rm -rf out
fi

# Clean old JAR
if [ -f "tp2.jar" ]; then
    echo "Removing old tp2.jar..."
    rm tp2.jar
fi

# Create output directory
echo "Creating out directory..."
mkdir -p out

# Compile Java files
echo "Compiling Java source files..."
javac -d out src/*.java

if [ $? -ne 0 ]; then
    echo "ERROR: Compilation failed"
    exit 1
fi

echo "Compilation successful!"

# Create executable JAR
echo "Creating executable JAR tp2.jar..."
jar cfe tp2.jar src.Main -C out .

if [ $? -ne 0 ]; then
    echo "ERROR: JAR creation failed"
    exit 1
fi

echo "JAR created successfully!"
echo ""
echo "=== Build Complete ==="
echo ""
echo "To run the application, use:"
echo "  java -Dn=<N> -Dp=<P> -Ds=<S> -Dt=<T> -jar tp2.jar"
echo ""
echo "Example:"
echo "  java -Dn=2 -Dp=2 -Ds=3 -Dt=100 -jar tp2.jar"
echo ""
