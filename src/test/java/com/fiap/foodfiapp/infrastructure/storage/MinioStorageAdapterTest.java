package com.fiap.foodfiapp.infrastructure.storage;

import io.minio.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MinioStorageAdapterTest {

    @Mock
    private MinioClient minioClient;

    private MinioStorageAdapter minioStorageAdapter;

    private static final String BUCKET_NAME = "test-bucket";
    private static final String PUBLIC_ENDPOINT = "http://localhost:9000";
    private static final String FILE_NAME = "test-file.jpg";
    private static final String CONTENT_TYPE = "image/jpeg";

    @BeforeEach
    void setUp() {
        minioStorageAdapter = new MinioStorageAdapter(minioClient);
        ReflectionTestUtils.setField(minioStorageAdapter, "bucketName", BUCKET_NAME);
        ReflectionTestUtils.setField(minioStorageAdapter, "minioPublicEndpoint", PUBLIC_ENDPOINT);
    }

    @Test
    void shouldStoreFileSuccessfully() throws Exception {
        // Arrange
        byte[] content = "test content".getBytes();
        InputStream inputStream = new ByteArrayInputStream(content);
        long size = content.length;

        when(minioClient.bucketExists(any(BucketExistsArgs.class))).thenReturn(true);
        when(minioClient.putObject(any(PutObjectArgs.class))).thenReturn(mock(ObjectWriteResponse.class));

        // Act
        String result = minioStorageAdapter.store(inputStream, size, CONTENT_TYPE, FILE_NAME);

        // Assert
        assertThat(result).isEqualTo(String.format("%s/%s/%s", PUBLIC_ENDPOINT, BUCKET_NAME, FILE_NAME));
        verify(minioClient).bucketExists(any(BucketExistsArgs.class));
        verify(minioClient).putObject(any(PutObjectArgs.class));
    }

    @Test
    void shouldCreateBucketIfNotExists() throws Exception {
        // Arrange
        byte[] content = "test content".getBytes();
        InputStream inputStream = new ByteArrayInputStream(content);
        long size = content.length;

        when(minioClient.bucketExists(any(BucketExistsArgs.class))).thenReturn(false);
        // makeBucket é void, então podemos usar doNothing()
        doNothing().when(minioClient).makeBucket(any(MakeBucketArgs.class));
        when(minioClient.putObject(any(PutObjectArgs.class))).thenReturn(mock(ObjectWriteResponse.class));

        // Act
        String result = minioStorageAdapter.store(inputStream, size, CONTENT_TYPE, FILE_NAME);

        // Assert
        assertThat(result).isNotNull();
        verify(minioClient).bucketExists(any(BucketExistsArgs.class));
        verify(minioClient).makeBucket(any(MakeBucketArgs.class));
        verify(minioClient).putObject(any(PutObjectArgs.class));
    }

    @Test
    void shouldThrowIOExceptionWhenStoreFailsDueToBucketError() throws Exception {
        // Arrange
        byte[] content = "test content".getBytes();
        InputStream inputStream = new ByteArrayInputStream(content);
        long size = content.length;

        when(minioClient.bucketExists(any(BucketExistsArgs.class)))
                .thenThrow(new RuntimeException("Bucket check failed"));

        // Act & Assert
        assertThatThrownBy(() -> minioStorageAdapter.store(inputStream, size, CONTENT_TYPE, FILE_NAME))
                .isInstanceOf(IOException.class)
                .hasMessageContaining("Failed to create bucket in MinIO");
    }

    @Test
    void shouldThrowIOExceptionWhenStoreFailsDueToPutObjectError() throws Exception {
        // Arrange
        byte[] content = "test content".getBytes();
        InputStream inputStream = new ByteArrayInputStream(content);
        long size = content.length;

        when(minioClient.bucketExists(any(BucketExistsArgs.class))).thenReturn(true);
        doThrow(new RuntimeException("Put object failed"))
                .when(minioClient).putObject(any(PutObjectArgs.class));

        // Act & Assert
        assertThatThrownBy(() -> minioStorageAdapter.store(inputStream, size, CONTENT_TYPE, FILE_NAME))
                .isInstanceOf(IOException.class)
                .hasMessageContaining("Failed to store file in MinIO");
    }

    @Test
    void shouldDeleteFileSuccessfully() throws Exception {
        // Arrange
        doNothing().when(minioClient).removeObject(any(RemoveObjectArgs.class));

        // Act
        minioStorageAdapter.delete(FILE_NAME);

        // Assert
        verify(minioClient).removeObject(any(RemoveObjectArgs.class));
    }

    @Test
    void shouldThrowIOExceptionWhenDeleteFails() throws Exception {
        // Arrange
        doThrow(new RuntimeException("Delete failed"))
                .when(minioClient).removeObject(any(RemoveObjectArgs.class));

        // Act & Assert
        assertThatThrownBy(() -> minioStorageAdapter.delete(FILE_NAME))
                .isInstanceOf(IOException.class)
                .hasMessageContaining("Failed to delete file from MinIO");
    }

    @Test
    void shouldGetFileUrlCorrectly() {
        // Act
        String result = minioStorageAdapter.getFileUrl(FILE_NAME);

        // Assert
        assertThat(result).isEqualTo(String.format("%s/%s/%s", PUBLIC_ENDPOINT, BUCKET_NAME, FILE_NAME));
        verifyNoInteractions(minioClient);
    }
}
