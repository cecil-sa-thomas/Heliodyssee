package fr.thomascecil.heliodysse.adapter.in.dto.response.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserResponseDTO {

    /* propriétés "safe" à renvoyer au client */

    @NotBlank(message = "Last name Can't be blank")
    private String lastName;

    @NotBlank(message = "First name Can't be blank")
    private String firstName;

    @NotBlank(message = "Email can't be blank")
    @Email(message = "Email not valid")
    private String email;

    private LocalDate dateOfBirth;

    private String phone;

}

