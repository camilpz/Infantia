package com.inf.payments.controllers;

import com.inf.payments.clients.StripeClient;
import com.inf.payments.dtos.Product;
import com.inf.payments.dtos.StripeSessionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private StripeClient stripeClient;

    @PostMapping("/pay")
    public ResponseEntity<StripeSessionResponse> initiatePayment(@RequestBody Product product) {
        try {
            var rta = stripeClient.checkout(product);
            return ResponseEntity.ok(rta);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
