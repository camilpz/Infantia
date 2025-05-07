package com.inf.users.clients.requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PutTutorRequest {
    private String firstName;
    private String lastName;
    private String address;
    private String postalCode;
    private String relationshipToChild;
    private String city;
}
