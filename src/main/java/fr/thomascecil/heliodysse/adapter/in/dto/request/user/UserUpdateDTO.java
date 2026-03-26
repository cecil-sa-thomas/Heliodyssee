package fr.thomascecil.heliodysse.adapter.in.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public class UserUpdateDTO {
    @NotBlank(message = "Last name Can't be blank")
    private String lastName;

    @NotBlank(message = "First name Can't be blank")
    private String firstName;

    @NotBlank(message = "Email can't be blank")
    @Email(message = "Email not valid")
    private String email;

    private LocalDate dateOfBirth;

    private String phone;

    @NotBlank(message = "Password Required")
    private String password;

    @NotBlank(message = "Old password Required")
    private String oldPassword;
}
