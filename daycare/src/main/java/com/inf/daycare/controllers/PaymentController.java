package com.inf.daycare.controllers;

import com.inf.daycare.clients.StripeClient;
import com.inf.daycare.dtos.pays.StripeProduct;
import com.inf.daycare.dtos.pays.StripeSessionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final StripeClient stripeClient;

    @PostMapping("/pay")
    public ResponseEntity<StripeSessionResponse> initiatePayment(@RequestBody StripeProduct stripeProduct) {
        try {
            var rta = stripeClient.checkout(stripeProduct);
            return ResponseEntity.ok(rta);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
