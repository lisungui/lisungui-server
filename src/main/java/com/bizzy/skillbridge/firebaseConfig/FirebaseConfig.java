package com.bizzy.skillbridge.firebaseConfig;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import io.github.cdimascio.dotenv.Dotenv;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class FirebaseConfig {
    
    @Bean
    public Firestore firestore() throws IOException {
        Dotenv dotenv = Dotenv.load();
        String serviceAccountPath = dotenv.get("FIRESTORE_APPLICATION_CREDENTIALS");
        if (serviceAccountPath == null || serviceAccountPath.isEmpty()) {
            throw new IOException("The path to the service account key file is not set.");
        }

        InputStream serviceAccount = new FileInputStream(serviceAccountPath);

        FirebaseOptions options = new FirebaseOptions.Builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .build();

        if (FirebaseApp.getApps().isEmpty()) { // check if existing apps are already initialized
            FirebaseApp.initializeApp(options);
        }
        return FirestoreClient.getFirestore();
    }
}
