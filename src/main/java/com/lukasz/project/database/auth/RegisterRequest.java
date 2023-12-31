package com.lukasz.project.database.auth;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Name can't be blank")
    @NotNull(message = "Name is required")
    @NotEmpty(message = "name should not be empty")
    private String name;

    @NotBlank(message = "Surname can't be blank")
    @NotNull(message = "Surname is required")
    @NotEmpty(message = "surname should not be empty")
    private String surname;

    @NotBlank(message = "Pesel can't be blank")
    @NotNull(message = "Pesel is required")
    @NotEmpty(message = "pesel should not be empty")
    private String pesel;

    @NotBlank(message = "Country can't be blank")
    @NotNull(message = "Country is required")
    @NotEmpty(message = "country should not be empty")
    private String country;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email can't be blank")
    @NotNull(message = "Email is required")
    @NotEmpty(message = "email should not be empty")
    private String email;

    @NotBlank(message = "Username can't be blank")
    @NotNull(message = "Username is required")
    @NotEmpty(message = "username should not be empty")
    private String username;

    @NotBlank(message = "Password can't be blank")
    @NotNull(message = "Password is required")
    @NotEmpty(message = "password should not be empty")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

}
