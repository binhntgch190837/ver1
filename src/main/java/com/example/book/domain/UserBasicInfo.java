package com.example.book.domain;

import com.example.book.security.ValidPassword;
import com.example.book.system.ValidImageURL;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.UUID;

@Data
public class UserBasicInfo {
    @JsonProperty
    private UUID id;

    @JsonProperty
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
    private String phone_number;

    @JsonProperty
    @ValidPassword
    @NotEmpty(message = "This field cannot be empty.")
    private String password;

    @JsonProperty
    @NotEmpty(message = "This field cannot be empty.")
    private String confirm_password;
}
