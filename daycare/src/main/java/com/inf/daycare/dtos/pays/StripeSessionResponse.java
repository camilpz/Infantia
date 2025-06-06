package com.inf.daycare.dtos.pays;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StripeSessionResponse{
    String status;
    String sessionId;
    String sessionUrl;
    String message;
}