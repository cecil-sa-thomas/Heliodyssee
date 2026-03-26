package fr.thomascecil.heliodysse.domain.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCard {
    private Long idPaymentCard;
    private String stripeId;
    private String lastDigit;
    private String brand;
    private Integer expMonth;
    private Integer expYear;
    private Boolean isDefault;
    private LocalDateTime dateCreation;
    private String createdBy;
    private LocalDateTime lastModificationDate;
    private String lastModificationBy;
    private Short version;

    private Long idUser;
}
