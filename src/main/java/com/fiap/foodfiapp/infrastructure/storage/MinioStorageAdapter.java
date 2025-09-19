package com.fiap.foodfiapp.infrastructure.storage;

import com.fiap.foodfiapp.core.domain.port.FileStorageRepository;
import io.minio.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class MinioStorageAdapter implements FileStorageRepository {
    private final MinioClient minioClient;

    @Value("${minio.bucket.name}")
    private String bucketName;

    @Value("${minio.endpoint}")
    private String minioEndpoint;

    @Override
    public String store(MultipartFile file, String fileName) throws IOException {
        try {
            createBucketIfNotExists();
            
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());
            
            return getFileUrl(fileName);
        } catch (Exception e) {
            throw new IOException("Failed to store file in MinIO", e);
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
            throw new IOException("Failed to delete file from MinIO", e);
        }
    }

    @Override
    public String getFileUrl(String fileName) {
        return String.format("%s/%s/%s", minioEndpoint, bucketName, fileName);
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
            throw new IOException("Failed to create bucket", e);
        }
    }
}
