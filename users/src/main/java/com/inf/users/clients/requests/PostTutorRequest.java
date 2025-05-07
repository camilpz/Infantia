package com.inf.users.clients.requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostTutorRequest {
    private Long userId;
    private String firstName;
    private String lastName;
    private String address;
    private String postalCode;
    private String relationshipToChild;
    private String city;
}
