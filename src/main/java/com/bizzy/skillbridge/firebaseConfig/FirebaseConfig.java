package com.bizzy.skillbridge.firebaseConfig;

import com.bizzy.skillbridge.configKey.ConfigKey;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


@Configuration
public class FirebaseConfig {
    
    @Bean
    public Firestore firestore() throws IOException {
        String apiKey = ConfigKey.getApiKey();

        if (apiKey == null || apiKey.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "API key not found. Value: " + apiKey);
        }
        FileInputStream serviceAccount = new FileInputStream(apiKey);
        

        FirebaseOptions options = new FirebaseOptions.Builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .build();

            if (FirebaseApp.getApps().isEmpty()) { // check with existing apps are already initialized
                FirebaseApp.initializeApp(options);
                }
        return FirestoreClient.getFirestore();
    }
}
