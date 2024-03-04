package com.example.book.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "rates")
@Getter
@Setter
@RequiredArgsConstructor
public class Rate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private int rate_value;

    @Column
    private LocalDateTime created_time;

    @ManyToOne
    @JoinColumn(name = "post_rate", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_rate", nullable = false)
    private User user;

    public Rate(User user, Post post){
        this.user = user;
        this.post = post;
    }
}
