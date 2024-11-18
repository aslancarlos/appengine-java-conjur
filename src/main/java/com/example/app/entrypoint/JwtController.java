package com.example.app.entrypoint;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequiredArgsConstructor
public class JwtController {

    @GetMapping("/jwt")
    public String getJwt() {
        // 1. Verificar se a variável de ambiente JWT existe
        String jwtFromEnv = System.getenv("JWT");
        if (jwtFromEnv != null && !jwtFromEnv.isEmpty()) {
            // Se a variável de ambiente JWT estiver definida, retornar o valor para fins de debug
            System.out.println("Usando JWT da variável de ambiente.");
            return jwtFromEnv;
        }

        // 2. Se a variável de ambiente não estiver definida, fazer a chamada ao Google Metadata Service
        String audience = "conjur/conjur/host/data/google/appengine/java"; // Substituir pelo público desejado
        String url = "http://metadata.google.internal/computeMetadata/v1/instance/service-accounts/default/identity?audience=" + audience;

        HttpHeaders headers = new HttpHeaders();
        headers.add("Metadata-Flavor", "Google");

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        System.out.println("Obtendo JWT do Google Metadata Service.");
        return response.getBody(); // Retorna o JWT como uma string
    }
}
