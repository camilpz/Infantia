package com.inf.users.clients;

import com.inf.users.clients.records.TutorRecord;
import com.inf.users.clients.requests.PostTutorRequest;
import com.inf.users.clients.requests.PutTutorRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class FamilyClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${family.service.url}")
    private String familyServiceUrl;

    private final String baseUrl = "http://localhost:8082/api/tutors";

    public TutorRecord createTutor(PostTutorRequest postTutorRequest){
        WebClient webClient = webClientBuilder.baseUrl(familyServiceUrl).build();

        return webClient.post()
                .uri("/api/tutors/create")
                .bodyValue(postTutorRequest)
                .retrieve()
                .bodyToMono(TutorRecord.class)
                .block();
    }

    public TutorRecord updateTutor(Long tutorId, PutTutorRequest putTutorRequest){
        WebClient webClient = webClientBuilder.baseUrl(familyServiceUrl).build();

        return webClient.put()
                .uri("/api/tutors/update/{tutorId}", tutorId)
                .bodyValue(putTutorRequest)
                .retrieve()
                .bodyToMono(TutorRecord.class)
                .block();
    }
}
