package com.example.book.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AdvertisementDetails {
    @JsonProperty
    private int id;

    @JsonProperty
    private String title;

    @JsonProperty
    private String company_name;

    @JsonProperty
    private String company_email;

    @JsonProperty
    private String status;

    @JsonProperty
    private String image_url;

    @JsonProperty
    private String link_url;

    @JsonProperty
    private String duration;
}
