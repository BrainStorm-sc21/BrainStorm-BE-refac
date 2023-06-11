package com.brainstrom.meokjang.notice;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Scanner;

@Configuration
public class FCMConfig {
    @Bean
    FirebaseMessaging firebaseMessaging() throws IOException {
        ClassPathResource resource = new ClassPathResource("firebase-adminsdk.json");
        System.out.println(resource.getFilename());
        System.out.println(resource);
        InputStream refreshToken = resource.getInputStream();
        System.out.println(refreshToken);

        FirebaseApp firebaseApp = null;
        List<FirebaseApp> firebaseAppList = FirebaseApp.getApps();

        if (firebaseAppList != null && !firebaseAppList.isEmpty()) {
            for (FirebaseApp app : firebaseAppList) {
                if (app.getName().equals(FirebaseApp.DEFAULT_APP_NAME)) {
                    firebaseApp = app;
                }
            }
        } else {
            Scanner scanner = new Scanner(refreshToken).useDelimiter("\\A");
            String json = scanner.hasNext() ? scanner.next() : "";
            JsonReader jsonReader = new JsonReader(new StringReader(json));
            jsonReader.setLenient(true);
            JsonObject jsonObject = new Gson().fromJson(jsonReader, JsonObject.class);

            GoogleCredentials credentials = GoogleCredentials.fromStream(new ByteArrayInputStream(json.getBytes()));

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(credentials)
                    .build();

            firebaseApp = FirebaseApp.initializeApp(options);
        }

        return FirebaseMessaging.getInstance(firebaseApp);
    }
}