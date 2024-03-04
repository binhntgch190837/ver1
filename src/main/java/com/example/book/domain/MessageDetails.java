package com.example.book.domain;

import com.example.book.entity.Chat;
import com.example.book.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.UUID;


@Data
public class MessageDetails {
    @JsonProperty
    private UUID id;

    @JsonProperty
    private String text;

    @JsonProperty
    private String last_updated;

    @JsonProperty
    private User sender;

    @JsonProperty
    private Chat chat;
}
