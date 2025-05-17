package com.example.email_service.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmailService {

    @Value("${resendapi}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public EmailService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendEmail(String to, String subject, String text) {
        String url = "https://api.resend.com/emails";

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        // Create the request body (JSON payload)
        String requestBody = String.format(
                "{\"from\": \"noreply@codezyra.com\", " +
                        "\"to\": \"%s\", " +
                        "\"subject\": \"%s\", " +
                        "\"text\": \"%s\"}",
                to, subject, text
        );

        // Wrap headers and body in an HttpEntity
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        // Send the request to Resend API
        try {
            restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            System.out.println("Email sent successfully to " + to);
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }
}
