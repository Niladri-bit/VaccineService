package com.assignment.vaccinationservice.service;


import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.assignment.vaccinationservice.DTOs.StudentRegisterDTO;
//
//import com.assignment.order.service.OrderService.DTO.BookDTO;
//import com.assignment.order.service.OrderService.DTO.StockUpdateRequestDTO;
//import com.assignment.order.service.OrderService.exceptions.BookNotFoundException;

@Service
public class UserCommunicationService {

    @Autowired
    private RestTemplate restTemplate;

  
    public List<StudentRegisterDTO> fetchStudentsByClasses(String classNames, String token) {
        
        String url = "http://localhost:8080/students?studentClass=" + classNames;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<StudentRegisterDTO[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                StudentRegisterDTO[].class
        );

        return Arrays.asList(response.getBody());
    }

    public StudentRegisterDTO fetchStudentById(Long id, String token) {
        String url = "http://localhost:8080/students/" + id;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<StudentRegisterDTO> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                StudentRegisterDTO.class
        );

        return response.getBody();
    }


}

