package com.bizzy.skillbridge.firebaseConfig;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
public class FirebaseConfig {

    @Bean
    public Firestore firestore() throws IOException {
        // Read the service account key JSON from an environment variable
        String serviceAccountKey = System.getenv("FIRESTORE_APPLICATION_CREDENTIALS_J");

        // Check if the environment variable is set
        if (serviceAccountKey == null) {
            throw new IllegalArgumentException("Environment variable FIRESTORE_APPLICATION_CREDENTIALS_J not set");
        }

        // Create credentials using the service account key JSON
        GoogleCredentials credentials = GoogleCredentials.fromStream(
            new ByteArrayInputStream(serviceAccountKey.getBytes(StandardCharsets.UTF_8))
        );

        // Initialize Firebase options
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(credentials)
                .build();

        // Initialize Firebase app if not already initialized
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }

        return FirestoreClient.getFirestore();
    }
}


