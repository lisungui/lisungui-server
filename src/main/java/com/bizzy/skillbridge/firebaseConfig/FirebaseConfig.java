package com.bizzy.skillbridge.firebaseConfig;

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


@Configuration
public class FirebaseConfig {
    
    @Bean
    public Firestore firestore() throws IOException {
        FileInputStream serviceAccount = new FileInputStream(".env");
        

        FirebaseOptions options = new FirebaseOptions.Builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .build();

            if (FirebaseApp.getApps().isEmpty()) { // check with existing apps are already initialized
                FirebaseApp.initializeApp(options);
                }
        return FirestoreClient.getFirestore();
    }
}
