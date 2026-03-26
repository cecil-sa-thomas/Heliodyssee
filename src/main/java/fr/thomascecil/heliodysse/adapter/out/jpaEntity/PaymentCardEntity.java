package fr.thomascecil.heliodysse.adapter.out.jpaEntity;

import fr.thomascecil.heliodysse.domain.model.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment_card")
@Data
public class PaymentCardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_payment_card")
    private Long idPaymentCard;

    @Column(name = "id_stripe", nullable = false, unique = true, length = 50)
    private String stripeId;

    @Column(name = "last_digit", nullable = false, length = 4)
    private String lastDigit;

    @Column(name = "brand", length = 20)
    private String brand;

    @Column(name = "exp_month", nullable = false)
    private Short expMonth;

    @Column(name = "exp_year")
    private Short expYear;

    @Column(name = "is_default")
    private Boolean isDefault;

    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @Column(name = "created_by", nullable = false, updatable = false, length = 50)
    private String createdBy;

    @Column(name = "last_modification_date")
    private LocalDateTime lastModificationDate;

    @Column(name = "last_modification_by", length = 50)
    private String lastModificationBy;

    @Version
    @Column(name = "version", nullable = false)
    private Short version;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private UserEntity user;
}
