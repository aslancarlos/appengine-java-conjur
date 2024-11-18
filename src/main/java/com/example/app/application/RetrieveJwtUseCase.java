package com.example.app.application;

import com.example.app.domain.JwtToken;
import com.example.app.infrastructure.GoogleTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RetrieveJwtUseCase {

    private final GoogleTokenProvider tokenProvider;

    public JwtToken execute(String targetAudience) {
        String token = tokenProvider.fetchToken(targetAudience);
        return new JwtToken(token);
    }
}

