package com.fiap.foodfiapp.core.domain.port;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface FileStorageRepository {
    String store(MultipartFile file, String fileName) throws IOException;
    void delete(String fileName) throws IOException;
    String getFileUrl(String fileName);
}
