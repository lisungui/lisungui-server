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

        FirebaseOptions options;

        // Check if running locally by looking for an environment variable or system property
        String environment = System.getenv("ENVIRONMENT");
        if (environment == null || environment.equals("LOCAL")) {
            // Load credentials from Dotenv for local development
            Dotenv dotenv = Dotenv.load();
            String firebaseConfigPath = dotenv.get("FIRESTORE_APPLICATION_CREDENTIALS");
            InputStream serviceAccount = new FileInputStream(firebaseConfigPath);

            options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();
        } else {
            // Use Application Default Credentials for GCP
            options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.getApplicationDefault())
                .build();
        }

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }

        return FirestoreClient.getFirestore();
    }
}
