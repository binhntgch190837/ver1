package com.example.book.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_histories")
@Getter
@Setter
@NoArgsConstructor
public class UserHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private LocalDateTime track_time;

    @Column
    private String action_detail;

    @ManyToOne
    @JoinColumn(name = "user_history", nullable = false)
    private User user;

    public UserHistory(User user, LocalDateTime track_time, String action_detail){
        this.user = user;
        this.track_time = track_time;
        this.action_detail = action_detail;
    }
}
