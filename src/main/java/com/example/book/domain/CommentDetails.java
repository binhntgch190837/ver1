package com.example.book.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CommentDetails {
    @JsonProperty
    private int id;

    @JsonProperty
    private String text;

    @JsonProperty
    private String updated_time;

    @JsonProperty
    private List<CommentDetails> replies;

    @JsonProperty
    private CommentDetails parent;

    @JsonProperty
    private UserInfoDetails creator;
}
