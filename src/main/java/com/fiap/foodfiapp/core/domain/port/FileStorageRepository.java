package com.fiap.foodfiapp.core.domain.port;

import java.io.IOException;
import java.io.InputStream;

public interface FileStorageRepository {
    /**
     * Armazena um arquivo.
     *
     * @param content O conteúdo do arquivo como um InputStream.
     * @param size O tamanho do arquivo em bytes.
     * @param contentType O tipo MIME do arquivo.
     * @param fileName O nome com o qual o arquivo será salvo.
     * @return A URL pública do arquivo armazenado.
     * @throws IOException Se ocorrer um erro durante o armazenamento.
     */
    String store(InputStream content, long size, String contentType, String fileName) throws IOException;

    void delete(String fileName) throws IOException;

    String getFileUrl(String fileName);
}