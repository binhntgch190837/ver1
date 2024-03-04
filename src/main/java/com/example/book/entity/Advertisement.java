package com.example.book.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "advertisements")
@Getter
@Setter
@RequiredArgsConstructor
public class Advertisement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String title;

    @Column
    private String company_name;

    @Column
    private String company_email;

    @Column
    private String status;

    @Column
    private String image_url;

    @Column
    private String link_url;

    @Column
    private LocalDateTime start_date;

    @Column
    private LocalDateTime end_date;
}
