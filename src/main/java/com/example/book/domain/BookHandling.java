package com.example.book.domain;

import com.example.book.system.ValidImageURL;
import com.example.book.system.ValidPDFURL;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class BookHandling {
    @JsonProperty
    private int id;

    @JsonProperty
    @NotEmpty(message = "This field not be empty.")
    private String title;

    @JsonProperty
    @ValidImageURL
    @NotEmpty(message = "This field not be empty.")
    private String image_url;

    @JsonProperty
    @NotEmpty(message = "This field cannot be empty.")
    private String author;

    @JsonProperty
    @NotEmpty(message = "Please select the published date.")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private String published_day;

    @JsonProperty
    @Min(value = 1, message = "Page number must be greater than or equal to 1.")
    @Max(value = 1000, message = "Page number must be less than or equal to 1000.")
    private int page;

    @JsonProperty
    @NotEmpty(message = "Please provide some description at least.")
    private String description;

    @JsonProperty
    @ValidPDFURL
    private String content;

    @JsonProperty
    @NotEmpty(message = "You must select a recommended age.")
    private String recommended_age;

    @JsonProperty
    @NotEmpty(message = "You must select a category.")
    private String category_name;
}
