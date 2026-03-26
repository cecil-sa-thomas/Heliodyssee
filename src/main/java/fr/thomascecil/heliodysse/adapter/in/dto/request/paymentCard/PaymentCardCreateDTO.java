package fr.thomascecil.heliodysse.adapter.in.dto.request.paymentCard;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PaymentCardCreateDTO {
    @NotBlank
    private  String paymentMethodId;
}
