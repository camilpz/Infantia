package com.inf.auth.dtos;

import java.util.List;

public record UserRecord
        (String id,
         String name,
         String email,
         String password,
         List<String> roles) {
}
