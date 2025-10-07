package com.fiap.foodfiapp.core.domain.port;

import java.io.IOException;

public interface FileStorageRepository {
    String store(String file, String fileName) throws IOException;
    void delete(String fileName) throws IOException;
    String getFileUrl(String fileName);
}
