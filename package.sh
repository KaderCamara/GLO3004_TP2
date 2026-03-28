#!/bin/bash

# Package script for TP2 submission
# Creates the submission archive with source code, JAR, and documentation

echo "=== Creating TP2 Submission Archive ==="

# Archive name
ARCHIVE_NAME="tp2-remise.zip"

# Check if JAR exists
if [ ! -f "tp2.jar" ]; then
    echo "ERROR: tp2.jar not found. Please run ./build.sh first"
    exit 1
fi

# Check if LisezMoi.txt exists
if [ ! -f "src/LisezMoi.txt" ]; then
    echo "ERROR: src/LisezMoi.txt not found"
    exit 1
fi

# Remove old archive if exists
if [ -f "$ARCHIVE_NAME" ]; then
    echo "Removing old archive..."
    rm "$ARCHIVE_NAME"
fi

# Create archive with source code, JAR, and documentation
echo "Creating archive $ARCHIVE_NAME..."
zip -r "$ARCHIVE_NAME" \
    src/*.java \
    src/LisezMoi.txt \
    tp2.jar \
    build.sh \
    -x "*.DS_Store" "*/.*"

if [ $? -ne 0 ]; then
    echo "ERROR: Archive creation failed"
    exit 1
fi

echo ""
echo "=== Archive Created Successfully! ==="
echo "File: $ARCHIVE_NAME"
echo ""
echo "Archive contents:"
unzip -l "$ARCHIVE_NAME"
echo ""
echo "You can now submit $ARCHIVE_NAME on the course portal"
