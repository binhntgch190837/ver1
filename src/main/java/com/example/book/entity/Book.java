package com.example.book.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "books")
@Getter
@Setter
@RequiredArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String title;

    @Column
    private String author;

    @Column
    private LocalDate published_date;

    @Column
    private int page;

    @Column
    private String description;

    @Column
    private String image_url;

    @Column
    private String content;

    @Column
    private int recommended_age;

    @ManyToOne
    @JoinColumn(name = "book_category", nullable = false)
    private Category category;

    @ManyToMany(mappedBy = "books")
    private Set<Favorite> favorites = new HashSet<>();
}
