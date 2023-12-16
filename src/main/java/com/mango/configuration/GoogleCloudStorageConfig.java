package com.mango.configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
public class GoogleCloudStorageConfig {
    @Value("${spring.cloud.gcp.storage.credentials.location}")
    private String location;
    @Bean
    public Storage storage() throws IOException {
        //byte[]  decodedKey = Base64.getDecoder().decode(encodedKey);
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