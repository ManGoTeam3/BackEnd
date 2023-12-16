package com.mango.configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Configuration
public class GoogleCloudStorageConfig {
    @Value("${spring.cloud.gcp.storage.credentials.encoded-key}")
    private String encodedKey;

    @Value("${spring.cloud.gcp.storage.credentials.location}")
    private String location;
    @Bean
    public Storage storage() throws IOException {
        //byte[]  decodedKey = Base64.getDecoder().decode(encodedKey);
        // URL-safe Base64에서 + 대신에 -를, / 대신에 _를 사용하도록 변환
        //String base64Key = encodedKey.replace('-', '+').replace('_', '/');
        // URL-safe Base64를 일반 Base64로 변환
        //byte[] decodedKey = Base64.getDecoder().decode(base64Key.getBytes(StandardCharsets.UTF_8));
        //ClassPathResource resource = new ClassPathResource("poetic-inkwell-401203-8aa220d61f5f.json");
        //GoogleCredentials credentials = GoogleCredentials.fromStream(new ByteArrayInputStream(decodedKey));
        ClassPathResource resource = new ClassPathResource(location);
        GoogleCredentials credentials = GoogleCredentials.fromStream(resource.getInputStream());
        String projectId = "poetic-inkwell-401203";
        return StorageOptions.newBuilder()
                .setProjectId(projectId)
                .setCredentials(credentials)
                .build()
                .getService();
    }
}