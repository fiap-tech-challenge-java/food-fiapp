package com.fiap.foodfiapp.core.application.dto;

import java.io.InputStream;

public record FileUploadRequest(
        InputStream content,
        long size,
        String contentType,
        String originalFilename
) {
    public FileUploadRequest {
        if (content == null) {
            throw new NullPointerException("Content cannot be null");
        }
        if (contentType == null) {
            throw new NullPointerException("Content type cannot be null");
        }
    }
}