# File Download Functionality

This document describes how to use the file download API in the Shadowise backend.

## Overview

The backend now supports downloading original uploaded files. Files are stored on the server filesystem and can be downloaded via an API endpoint.

## API Endpoints

### Download a File

```
GET /api/files/download/{id}
```

- `{id}`: The ID of the uploaded file to download

**Response:**
- If successful: The file will be downloaded with Content-Disposition: attachment
- If file not found: 404 Not Found with error message

### Example

To download a file with ID `688fca8349a0c00fd7efd811`, make a GET request to:

```
GET /api/files/download/688fca8349a0c00fd7efd811
```

## File Storage

Files are stored in the `file-storage` directory (configurable in `application.yml`). The storage path can be changed by modifying the `file.storage.path` property in your application.yml file.

## Implementation Details

1. When a file is uploaded via `/api/projects/create-with-files` endpoint:
   - The original file is saved to the filesystem
   - The file path is stored in the `UploadedFile` entity
   - The file can then be downloaded using the download endpoint

2. File metadata (name, type, path) is stored in the database
   - This allows for retrieval and management of files

## Integration with Frontend

For frontend integration, you can provide a download link using:

```html
<a href="https://your-api-url/api/files/download/FILE_ID" download>Download File</a>
```

Or you can implement a button that makes an AJAX request to the endpoint.