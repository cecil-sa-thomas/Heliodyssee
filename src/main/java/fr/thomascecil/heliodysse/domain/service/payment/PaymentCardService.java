package fr.thomascecil.heliodysse.domain.service.payment;

import fr.thomascecil.heliodysse.domain.model.entity.PaymentCard;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.YearMonth;

@Component
public class PaymentCardService {
    public void validatePaymentCard(PaymentCard paymentCard) {
        checkRequiredFields(paymentCard);
        initalizedDefaultsSelf(paymentCard);
        isDateValid(paymentCard);
    }

    public void checkRequiredFields(PaymentCard paymentCard) {
        if (paymentCard.getStripeId().isBlank()) {
            throw new IllegalArgumentException("Stripe id mandatory.");
        }
        if (paymentCard.getLastDigit().isBlank()) {
            throw new IllegalArgumentException("Last digit mandatory.");
        }
        if (paymentCard.getBrand().isBlank()) {
            throw new IllegalArgumentException("Brand passenger mandatory.");
        }
        if (paymentCard.getIsDefault() == null) {
            throw new IllegalArgumentException(("Default mandatory"));
        }

    }
    public void initalizedDefaultsSelf(PaymentCard paymentCard){

        if (paymentCard.getCreatedBy() == null) {
            paymentCard.setCreatedBy("placeHolder");
        }
        if (paymentCard.getVersion() == null) {
            paymentCard.setVersion((short) 0);
        }
        if (paymentCard.getDateCreation() == null) {
            paymentCard.setDateCreation(LocalDateTime.now());
        }
    }

    public void isDateValid(PaymentCard paymentCard) {
        if (paymentCard.getExpMonth() == null || paymentCard.getExpYear() == null) {
            throw new IllegalArgumentException("Expiration month and year are mandatory.");
        }

        int expMonth = paymentCard.getExpMonth();
        int expYear = paymentCard.getExpYear();

        if (expMonth < 1 || expMonth > 12) {
            throw new IllegalArgumentException("Expiration month must be between 1 and 12.");
        }

        YearMonth currentYearMonth = YearMonth.now();
        YearMonth cardYearMonth = YearMonth.of(expYear, expMonth);

        if (cardYearMonth.isBefore(currentYearMonth)) {
            throw new IllegalArgumentException("Payment card expiration date must be in the future.");
        }
    }

    //inutile : une carte ne s'update pas
    public void updateInfoSystem(PaymentCard paymentCard){
        paymentCard.setLastModificationDate(LocalDateTime.now());
        paymentCard.setLastModificationBy("system");
    }

}
