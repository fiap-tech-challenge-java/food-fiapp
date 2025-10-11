package com.fiap.foodfiapp.core.domain.port;

import java.io.IOException;
import java.io.InputStream;

public interface FileStorageRepository {

    String store(InputStream content, long size, String contentType, String fileName) throws IOException;

    void delete(String fileName) throws IOException;

    String getFileUrl(String fileName);
}