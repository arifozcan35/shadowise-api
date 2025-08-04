# File Handling in Shadowise API

## Overview

The Shadowise API handles files in a dual-storage approach:
1. Original files are stored locally for download
2. API-processed files are referenced for AI operations

## File Storage Approach

### Original Files
- Original uploaded files are stored in the local filesystem (`file-storage` directory)
- These files retain their original content and are available for download
- The path is stored in the `filePath` field of the `UploadedFile` entity

### API-Processed Files
- Files are also processed by the AI service for text extraction and analysis
- The paths to these processed files are stored in the `apiFilePath` field
- These paths point to files managed by the AI service

## API Endpoints

### Download Original File
```
GET /api/files/download/{id}
```
- Downloads the original file with its original name and type

### Get API File Path
```
GET /api/files/api-path/{id}
```
- Returns the API-processed file path as a string
- This path can be used by the AI service for further processing

## Usage in Code

When working with files, you can:

1. Access the original file for download:
```java
UploadedFile file = uploadedFileRepository.findById(id).orElse(null);
String originalFilePath = file.getFilePath();
```

2. Access the API file path for AI processing:
```java
UploadedFile file = uploadedFileRepository.findById(id).orElse(null);
String apiFilePath = file.getApiFilePath();
```

## Implementation

The file upload process now follows these steps:
1. Save original file to local storage
2. Send file to AI service for processing
3. Store both paths in the `UploadedFile` entity
4. Make both available through API endpoints