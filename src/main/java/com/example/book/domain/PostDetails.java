package com.example.book.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class PostDetails {
    @JsonProperty
    private UUID id;

    @JsonProperty
    private String post_id;

    @JsonProperty
    private UUID shared_post_id;

    @JsonProperty
    private String title;

    @JsonProperty
    private String content_image;

    @JsonProperty
    private String content_text;

    @JsonProperty
    private String last_updated;

    @JsonProperty
    private float average_rate;

    @JsonProperty
    private List<CommentDetails> comments;

    @JsonProperty
    private UserInfoDetails creator_detail;

    @JsonProperty
    private UserInfoDetails shared_user;

    @JsonProperty
    private String shared_time;
}
