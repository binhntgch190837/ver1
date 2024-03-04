package com.example.book.domain;

import com.example.book.entity.Category;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BookDetails {
    @JsonProperty
    private int id;

    @JsonProperty
    private String title;

    @JsonProperty
    private String author;

    @JsonProperty
    private LocalDate published_date;

    @JsonProperty
    private String published_day;

    @JsonProperty
    private int page;

    @JsonProperty
    private String description;

    @JsonProperty
    private String content;

    @JsonProperty
    private String image_url;

    @JsonProperty
    private int recommended_age;

    @JsonProperty
    private Category category;

    @JsonProperty
    private String category_name;
}
