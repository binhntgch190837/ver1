package com.example.book.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CategoryHandling {
    @JsonProperty
    private int id;

    @JsonProperty
    @NotEmpty(message = "Please type the category name.")
    private String name;
}
