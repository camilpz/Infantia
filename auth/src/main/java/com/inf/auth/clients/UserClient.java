package com.inf.auth.clients;

import com.inf.auth.dtos.UserRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserClient {
    //private final RestTemplate restTemplate;
    private static final String USER_SERVICE_URL = "users-service";

    public UserRecord getUserByEmail(String email) {
//        //url
//        String url = USER_SERVICE_URL + "/users/getByEmail/" + email;
//        //headers
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        //request
//        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
//
//        //response
//        ResponseEntity<UserRecord> response = restTemplate.exchange(
//                url,
//                HttpMethod.GET,
//                requestEntity,
//                UserRecord.class
//        );
//
//        if (!response.getStatusCode().is2xxSuccessful()) {
//            return null;
//        }

        return new UserRecord(
                "1",
                "Test User",
                email,
                "$2a$10$e0j5x5Q6Z5z5Z5Z5Z5Z5Z.0", // Simulated hashed password
                List.of("USER")
        );
    }
}
