package com.assignment.vaccinationservice.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.assignment.vaccinationservice.DTOs.TokenInformationDTO;


@Service
public class TokenValidationService {

    @Autowired
    private RestTemplate restTemplate;

    private static final String USER_SERVICE_URL = "http://localhost:8080/validateToken";

    public TokenInformationDTO retrieveUserDetailsFromToken(String token) {
        HttpHeaders headers = new HttpHeaders();
        System.out.println(token);
        headers.set("Authorization", token);

        HttpEntity<String> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<TokenInformationDTO> response = restTemplate.exchange(
            		USER_SERVICE_URL,
                    HttpMethod.GET,
                    request,
                    TokenInformationDTO.class
            );
            return response.getBody();

        } catch ( HttpStatusCodeException e) {
            System.err.println("Token validation failed: " + e.getStatusCode() + " " + e.getResponseBodyAsString());
            throw new RuntimeException("Invalid token or unauthorized access", e);
        }
    }
}
