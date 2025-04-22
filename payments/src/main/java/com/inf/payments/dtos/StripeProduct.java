package com.inf.payments.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StripeProduct {
    private String name;
    private Long quantity;
    private Long amount;
    private String currency;
}
