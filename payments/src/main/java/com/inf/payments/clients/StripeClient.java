package com.inf.payments.clients;

import com.inf.payments.dtos.Product;
import com.inf.payments.dtos.StripeSessionResponse;
import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@NoArgsConstructor
public class StripeClient {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    public StripeSessionResponse checkout(Product product) {
        Stripe.apiKey = stripeApiKey;

        SessionCreateParams.LineItem.PriceData.ProductData productData =
                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                        .setName(product.getName())
                        .build();

        SessionCreateParams.LineItem.PriceData priceData =
                SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency(product.getCurrency())
                        .setUnitAmount(product.getAmount() * 100)
                        .setProductData(productData)
                        .build();

        SessionCreateParams.LineItem lineItem =
                SessionCreateParams.LineItem.builder()
                        .setPriceData(priceData)
                        .setQuantity(product.getQuantity())
                        .build();

        SessionCreateParams params =
                SessionCreateParams.builder()
                        .addLineItem(lineItem)
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl("https://youtu.be/G-_4HKPqLso?si=Rr2NS81TNmGOCt2N")
                        .setCancelUrl("https://youtu.be/xvFZjo5PgG0?si=jtYmd2ZPaq_t2nhQ")
                        .build();

        Session session = null;

        try{
            session = Session.create(params);
            System.out.println("Session created successfully: " + session.getId());
            System.out.println("Session URL: " + session.getUrl());
            System.out.println("Session status: " + session.getStatus());
            System.out.println("Session amount total: " + session.getAmountTotal());
            System.out.println("Session customer" + session.getCustomer());
            System.out.println("Session customer" + session.getCustomer());

            return StripeSessionResponse.builder()
                    .status("SUCCESS")
                    .message("Session created successfully")
                    .sessionId(session.getId())
                    .sessionUrl(session.getUrl())
                    .build();
        }
        catch (Exception e) {
            System.out.println("Error creating Stripe session: " + e.getMessage());
            throw new RuntimeException("Error creating Stripe session: " + e.getMessage());
        }
    }
}
