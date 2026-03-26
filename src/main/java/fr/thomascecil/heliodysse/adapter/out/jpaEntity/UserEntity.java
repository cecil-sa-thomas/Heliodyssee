package fr.thomascecil.heliodysse.adapter.out.jpaEntity;


import fr.thomascecil.heliodysse.domain.model.enums.userEnum.UserRole;
import fr.thomascecil.heliodysse.domain.model.enums.userEnum.UserStatus;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "user_", uniqueConstraints = @UniqueConstraint(columnNames = "mail"))
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long idUser;

    @Column(name = "stripe_customer_id", length = 50, unique = true)
    private String stripeCustomerId;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "email", nullable = false, length = 50, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private UserStatus status;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "phone", nullable = false, length = 50)
    private String phone;

    @Column(name = "password", nullable = false, length = 60)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 50)
    private UserRole role;

    @Column(name = "activation_token", length = 255)
    private String activationToken;

    @Column(name = "activation_token_expiration")
    private LocalDateTime activationTokenExpiration;

    @Column(name = "last_connected")
    private LocalDateTime lastConnected;

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime dateCreation;

    @Column(name = "created_by", nullable = false, length = 50)
    private String createdBy;

    @Column(name = "last_modification_date")
    private LocalDateTime lastModificationDate;

    @Column(name = "last_modification_by", length = 50)
    private String lastModificationBy;

    @Version
    @Column(name = "version", nullable = false)
    private Short version;
}