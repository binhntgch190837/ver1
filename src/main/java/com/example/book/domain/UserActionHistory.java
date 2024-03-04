package com.example.book.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserActionHistory {
    @JsonProperty
    private int id;

    @JsonProperty
    private String action_detail;

    @JsonProperty
    private String action_time;
}
