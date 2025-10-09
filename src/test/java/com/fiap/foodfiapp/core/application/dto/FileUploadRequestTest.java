package com.fiap.foodfiapp.core.application.dto;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class FileUploadRequestTest {

    @Test
    void shouldCreateFileUploadRequestWithAllFields() {
        // Arrange
        String content = "test content";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes());
        long size = content.length();
        String contentType = "image/jpeg";
        String originalFilename = "test.jpg";

        // Act
        FileUploadRequest request = new FileUploadRequest(
            inputStream,
            size,
            contentType,
            originalFilename
        );

        // Assert
        assertNotNull(request);
        assertEquals(inputStream, request.content());
        assertEquals(size, request.size());
        assertEquals(contentType, request.contentType());
        assertEquals(originalFilename, request.originalFilename());
    }

    @Test
    void shouldAllowNullOriginalFilename() {
        // Arrange
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        
        // Act
        FileUploadRequest request = new FileUploadRequest(
            inputStream,
            4,
            "text/plain",
            null
        );

        // Assert
        assertNull(request.originalFilename());
    }

    @Test
    void shouldNotAllowNullContent() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> 
            new FileUploadRequest(
                null,
                0,
                "text/plain",
                "test.txt"
            )
        );
    }

    @Test
    void shouldNotAllowNullContentType() {
        // Arrange
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        
        // Act & Assert
        assertThrows(NullPointerException.class, () -> 
            new FileUploadRequest(
                inputStream,
                4,
                null,
                "test.txt"
            )
        );
    }

    @Test
    void shouldHandleZeroSize() {
        // Arrange
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        
        // Act
        FileUploadRequest request = new FileUploadRequest(
            inputStream,
            0,
            "application/octet-stream",
            "empty.bin"
        );

        // Assert
        assertEquals(0, request.size());
    }

    @Test
    void shouldHandleLargeFileSize() {
        // Arrange
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        long largeSize = 1024L * 1024 * 1024 * 2; // 2GB
        
        // Act
        FileUploadRequest request = new FileUploadRequest(
            inputStream,
            largeSize,
            "application/octet-stream",
            "largefile.bin"
        );

        // Assert
        assertEquals(largeSize, request.size());
    }

    @Test
    void shouldReadContentFromInputStream() throws IOException {
        // Arrange
        String content = "test content";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes());
        
        // Act
        FileUploadRequest request = new FileUploadRequest(
            inputStream,
            content.length(),
            "text/plain",
            "test.txt"
        );

        // Assert
        byte[] buffer = new byte[content.length()];
        int bytesRead = request.content().read(buffer);
        
        assertEquals(content.length(), bytesRead);
        assertEquals(content, new String(buffer));
    }
}
