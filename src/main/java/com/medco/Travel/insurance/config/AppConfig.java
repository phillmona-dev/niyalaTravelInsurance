package com.medco.Travel.insurance.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;

import java.io.IOException;

@Configuration
public class AppConfig {

    @Value("${mapfre.api.key}")
    private String apiKey;

    @Value("${mapfre.api.url}")
    private String apiUrl;

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.getInterceptors().add(new ClientHttpRequestInterceptor() {
            @Override
            public org.springframework.http.client.ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
                HttpHeaders headers = request.getHeaders();
                headers.add("Authorization", "Bearer " + apiKey);
                return execution.execute(request, body);
            }
        });

        return restTemplate;
    }

    @Bean
    public String apiUrl() {
        return apiUrl;
    }
}
