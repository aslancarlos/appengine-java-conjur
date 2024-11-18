package com.example.app.entrypoint;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RestController
@RequiredArgsConstructor
public class ConjurController {

    private final JwtController jwtController; // Injeta o controlador JWT para reutilizar o método getJwt

    @Value("${conjur.auth.url}")
    private String conjurAuthUrl;

    @Value("${conjur.secrets.url}")
    private String conjurSecretsUrl;

    @GetMapping("/conjur")
    public String getSecretsFromConjur() {
        // 1. Obter o JWT do Google Metadata Service ou da variável de ambiente (no JwtController)
        String jwt = jwtController.getJwt();

        // 2. Autenticar no Conjur usando o JWT, no formato do curl fornecido
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders authHeaders = new HttpHeaders();
        authHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        authHeaders.add("Accept-Encoding", "base64");

        MultiValueMap<String, String> authRequestBody = new LinkedMultiValueMap<>();
        authRequestBody.add("jwt", jwt);

        HttpEntity<MultiValueMap<String, String>> authEntity = new HttpEntity<>(authRequestBody, authHeaders);

        ResponseEntity<String> authResponse = restTemplate.exchange(
            conjurAuthUrl,
            HttpMethod.POST,
            authEntity,
            String.class
        );

        if (!authResponse.getStatusCode().is2xxSuccessful()) {
            return "Falha ao autenticar no Conjur.";
        }

        // 3. Obter o token do Conjur e decodificar de base64
        String conjurAuthToken = authResponse.getBody();
       
        // 4. Configurar o cabeçalho de autorização para recuperar os segredos
        HttpHeaders secretHeaders = new HttpHeaders();
        secretHeaders.add("Authorization", "Token token=\"" + conjurAuthToken + "\"");

        // 5. Buscar dois segredos no Conjur (segredos em formato URL encode)
        //String secret1Path = URLEncoder.encode("data/vault/dev-demo-cred/appuser_db/username", StandardCharsets.UTF_8);
        //String secret2Path = URLEncoder.encode("data/vault/dev-demo-cred/appuser_db/password", StandardCharsets.UTF_8);

        // 5. Buscar dois segredos no Conjur (segredos em formato URL encode)
        String secret1Path = URLEncoder.encode("data/vault/dev-demo-cred/appuser_db/username", StandardCharsets.UTF_8).replace("%2F", "/");
        String secret2Path = URLEncoder.encode("data/vault/dev-demo-cred/appuser_db/password", StandardCharsets.UTF_8).replace("%2F", "/");
        String secret1 = getSecret(restTemplate, secretHeaders, conjurSecretsUrl + secret1Path);
        String secret2 = getSecret(restTemplate, secretHeaders, conjurSecretsUrl + secret2Path);

        // 6. Retornar os segredos
        return String.format("Secret 1 : %s%nSecret 2 : %s", secret1, secret2);
    }

    private String getSecret(RestTemplate restTemplate, HttpHeaders headers, String url) {
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            return "Falha ao obter o segredo: " + url;
        }
    }
}