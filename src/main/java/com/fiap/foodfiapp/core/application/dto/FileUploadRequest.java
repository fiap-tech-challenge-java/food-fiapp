package com.fiap.foodfiapp.core.application.dto;

import java.io.InputStream;

/**
 * DTO para transportar dados de upload de arquivo entre a camada de infraestrutura e a de aplicação,
 * sem acoplar a camada de aplicação a frameworks como o Spring (ex: MultipartFile).
 */
public record FileUploadRequest(
        InputStream content,
        long size,
        String contentType,
        String originalFilename
) {
}