package com.example.app.infrastructure;

import com.google.auth.oauth2.GoogleCredentials;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;

@Slf4j
@Component
public class GoogleTokenProvider {

    public String fetchToken(String targetAudience) {
        try {
            GoogleCredentials credentials = GoogleCredentials.getApplicationDefault()
                    .createScoped(Collections.singletonList("https://www.googleapis.com/auth/cloud-platform"));

            credentials = credentials.createScoped(Collections.emptyList());

            String token = credentials.refreshAccessToken().getTokenValue();
            log.debug("Successfully fetched JWT: {}", token);
            return token;
        } catch (IOException e) {
            log.error("Error fetching JWT from Google: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch Google JWT", e);
        }
    }
}

