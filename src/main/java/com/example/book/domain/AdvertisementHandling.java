package com.example.book.domain;

import com.example.book.system.ValidImageURL;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class AdvertisementHandling {
    @JsonProperty
    private int id;

    @JsonProperty
    @NotEmpty(message = "This field not be empty.")
    private String title;

    @JsonProperty
    @NotEmpty(message = "This field not be empty.")
    private String company_name;

    @JsonProperty
    @NotEmpty(message = "This field not be empty.")
    private String company_email;

    @JsonProperty
    @NotEmpty(message = "You must select a status.")
    private String status;

    @JsonProperty
    @ValidImageURL
    @NotEmpty(message = "This field not be empty.")
    private String image_url;

    @JsonProperty
    @NotEmpty(message = "This field not be empty.")
    private String link_url;

    @JsonProperty
    @NotEmpty(message = "Please select the start date.")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private String start_day;

    @JsonProperty
    @NotEmpty(message = "Please select the end date.")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private String end_day;

    @JsonProperty
    @NotEmpty(message = "Please select the start time.")
    @DateTimeFormat(pattern = "HH:mm:ss")
    private String start_time;

    @JsonProperty
    @NotEmpty(message = "Please select the end time.")
    @DateTimeFormat(pattern = "HH:mm:ss")
    private String end_time;
}
