package com.hiringbell.authenticator.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    private final static Logger LOGGER = LoggerFactory.getLogger(JwtUtil.class);
    private static final String SECRET_KEY = "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";

    public Map<String, Object> validateToken(String token) {
        // Claims claims = null;
        Map<String, Object> userDetails = new HashMap<>();
        Map<String, Object> claims = null;
        try {

            HttpTransport httpTransport = new NetHttpTransport();
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(httpTransport, JacksonFactory.getDefaultInstance())
                    .setAudience(Collections.singletonList("622966386962-pcep2a9p2l0j75p1nrl5m7clhlln3eil.apps.googleusercontent.com"))
                .build();

            // Verify it
            GoogleIdToken idToken = verifier.verify(token);

            GoogleIdToken.Payload payload = idToken.getPayload();
            String name = (String) payload.get("name");
            String email = payload.getEmail();

            userDetails.put("name", name);
            userDetails.put("email", email);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }

        return userDetails;
    }
}