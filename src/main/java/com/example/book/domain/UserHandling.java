package com.example.book.domain;

import com.example.book.security.ValidPassword;
import com.example.book.system.ValidImageURL;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class UserHandling {
    @JsonProperty
    private UUID id;

    @JsonProperty
    @Size(min = 2, max = 100, message = "Username name must be between 2 and 100 characters.")
    private String username;

    @JsonProperty
    @NotEmpty(message = "User image cannot be empty.")
    @ValidImageURL
    private String image_url;

    @JsonProperty
    @NotEmpty(message = "This field cannot be empty.")
    @Email
    private String email;

    @JsonProperty
    @NotEmpty(message = "This field cannot be empty.")
    @Size(max = 11, message = "Phone cannot exceed 11 numbers.")
    @Pattern(regexp = "\\d+", message = "Phone must contain only numeric characters.")
    private String phone_number;

    @JsonProperty
    @NotEmpty(message = "You must select a status.")
    private String status;

    @JsonProperty
    @ValidPassword
    @NotEmpty(message = "This field cannot be empty.")
    private String password;

    @JsonProperty
    private String confirm_password;

    @JsonProperty
    @NotEmpty(message = "You must select a role.")
    private String input_role;
}
