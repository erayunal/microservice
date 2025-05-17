package com.erayunal.config;

import feign.Response;
import feign.codec.ErrorDecoder;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

public class CustomErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        String errorMessage = "An unexpected error occurred";

        // Eğer yanıt gövdesi varsa
        if (response.body() != null) {
            try (Reader reader = new InputStreamReader(response.body().asInputStream(), StandardCharsets.UTF_8)) {
                // Hata mesajını body'den alıyoruz
                StringBuilder responseBody = new StringBuilder();
                int read;
                char[] buffer = new char[1024];
                while ((read = reader.read(buffer)) != -1) {
                    responseBody.append(buffer, 0, read);
                }
                errorMessage = responseBody.toString();
            } catch (IOException e) {
                // Eğer body okuma sırasında bir hata olursa, genel hata mesajı döndürüyoruz
                errorMessage = "Error reading the response body";
            }
        }

        // Exception'ı oluşturup hata mesajını döndürüyoruz
        return new RuntimeException(errorMessage);
    }
}
