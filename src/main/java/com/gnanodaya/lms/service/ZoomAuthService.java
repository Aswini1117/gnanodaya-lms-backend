package com.gnanodaya.lms.service;

import com.gnanodaya.lms.dto.zoom.ZoomTokenResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@Service
@Slf4j
public class ZoomAuthService {

    @Value("${zoom.account.id}")
    private String accountId;

    @Value("${zoom.client.id}")
    private String clientId;

    @Value("${zoom.client.secret}")
    private String clientSecret;

    @Value("${zoom.oauth.token.url}")
    private String tokenUrl;

    private String cachedToken;
    private long tokenExpiryTime;

    public String getAccessToken() {

        if (cachedToken != null &&
                System.currentTimeMillis() < tokenExpiryTime) {
            return cachedToken;
        }

        try {
            RestTemplate restTemplate = new RestTemplate();

            String credentials = clientId + ":" + clientSecret;
            String encodedCredentials = Base64.getEncoder()
                    .encodeToString(credentials.getBytes());

            log.info("Zoom Auth - Account ID: {}", accountId);
            log.info("Zoom Auth - Client ID: {}", clientId);
            log.info("Zoom Auth - Token URL: {}", tokenUrl);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.set("Authorization", "Basic " + encodedCredentials);

            MultiValueMap<String, String> body =
                    new LinkedMultiValueMap<>();
            body.add("grant_type", "account_credentials");
            body.add("account_id", accountId);

            HttpEntity<MultiValueMap<String, String>> request =
                    new HttpEntity<>(body, headers);

            log.info("Calling Zoom token URL: {}", tokenUrl);

            ResponseEntity<ZoomTokenResponse> response =
                    restTemplate.postForEntity(
                            tokenUrl,
                            request,
                            ZoomTokenResponse.class);

            log.info("Zoom token response status: {}",
                    response.getStatusCode());

            if (response.getStatusCode() == HttpStatus.OK
                    && response.getBody() != null) {

                cachedToken = response.getBody().getAccessToken();
                tokenExpiryTime = System.currentTimeMillis()
                        + (55 * 60 * 1000);

                log.info("Zoom access token obtained successfully");
                return cachedToken;
            }

            throw new RuntimeException(
                    "Empty response from Zoom token endpoint");

        } catch (HttpClientErrorException e) {
            log.error("Zoom Auth HTTP error: {} - Body: {}",
                    e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException(
                    "Zoom auth failed: " + e.getResponseBodyAsString());

        } catch (Exception e) {
            log.error("Zoom Auth exception: {}", e.getMessage(), e);
            throw new RuntimeException(
                    "Failed to authenticate with Zoom API: "
                            + e.getMessage());
        }
    }
}