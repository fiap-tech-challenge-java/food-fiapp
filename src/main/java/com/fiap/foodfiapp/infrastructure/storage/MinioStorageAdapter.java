package com.fiap.foodfiapp.infrastructure.storage;

import com.fiap.foodfiapp.core.domain.port.FileStorageRepository;
import io.minio.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class MinioStorageAdapter implements FileStorageRepository {
    private final MinioClient minioClient;

    @Value("${minio.bucket.name}")
    private String bucketName;

    @Value("${minio.public.endpoint}")
    private String minioPublicEndpoint;

    public MinioStorageAdapter(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @Override
    public String store(InputStream content, long size, String contentType, String fileName) throws IOException {
        try {
            createBucketIfNotExists();

            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .stream(content, size, -1)
                    .contentType(contentType)
                    .build());

            return getFileUrl(fileName);
        } catch (Exception e) {
            throw new IOException("Failed to store file in MinIO: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(String fileName) throws IOException {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .build());
        } catch (Exception e) {
            throw new IOException("Failed to delete file from MinIO: " + e.getMessage(), e);
        }
    }

    @Override
    public String getFileUrl(String fileName) {
        return String.format("%s/%s/%s", minioPublicEndpoint, bucketName, fileName);
    }

    private void createBucketIfNotExists() throws IOException {
        try {
            boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(bucketName)
                    .build());

            if (!bucketExists) {
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(bucketName)
                        .build());
            }
        } catch (Exception e) {
            throw new IOException("Failed to create bucket in MinIO: " + e.getMessage(), e);
        }
    }
}