package fr.thomascecil.heliodysse.domain.service.payment;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentMethodAttachParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class StripeService {

    @Value("${stripe.secret-key}")
    private String stripeSecretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }


    public PaymentIntent processPayment(String paymentMethodId, long amountCents) throws StripeException {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amountCents)
                .setCurrency("eur")
                .setPaymentMethod(paymentMethodId)
                .setConfirm(true)
                .build();

        return PaymentIntent.create(params);
    }

    public PaymentMethod addCardToCustomer(String customerId, String paymentMethodId) throws StripeException {
        // 1. Récupérer le PaymentMethod
        PaymentMethod paymentMethod = PaymentMethod.retrieve(paymentMethodId);

        // 2. L'attacher au customer
        PaymentMethodAttachParams params = PaymentMethodAttachParams.builder()
                .setCustomer(customerId)
                .build();

        return paymentMethod.attach(params);
    }

    public Customer createCustomer() throws StripeException {
        return Customer.create(new HashMap<>()); // appel vide minimal
    }
}