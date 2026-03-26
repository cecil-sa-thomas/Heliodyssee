package fr.thomascecil.heliodysse.domain.model.entity;

import fr.thomascecil.heliodysse.domain.model.enums.userEnum.UserRole;
import fr.thomascecil.heliodysse.domain.model.enums.userEnum.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long idUser;
    private String stripeCustomerId;
    private String lastName;
    private String firstName;
    private String email;
    private UserStatus status;
    private LocalDate dateOfBirth;
    private String phone;
    private String password;
    private UserRole role;
    private String activationToken;
    private LocalDateTime activationTokenExpiration;
    private LocalDateTime lastConnected;
    private LocalDateTime dateCreation;
    private String createdBy;
    private LocalDateTime lastModificationDate;
    private String lastModificationBy;
    private Short version;

}