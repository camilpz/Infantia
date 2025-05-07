package com.inf.users.clients.records;

public record TutorRecord(
         Long id,
         Long userId,
         String firstName,
         String lastName,
         String address,
         String postalCode,
         String relationshipToChild,
         Boolean enabled,
         String city
) { }
