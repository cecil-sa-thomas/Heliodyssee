package fr.thomascecil.heliodysse.adapter.in.dto.response.paymentCard;

import lombok.Data;

@Data
public class PaymentCardResponseDTO {
    private Long idPaymentCard;
    private String stripeId;
    private String lastDigit;
    private String brand;
    private Integer expMonth;
    private Integer expYear;
    private Boolean isDefault;
    private Long idUser;
}
