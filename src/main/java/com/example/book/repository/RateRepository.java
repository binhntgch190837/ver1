package com.example.book.repository;

import com.example.book.entity.Rate;
import com.example.book.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RateRepository extends JpaRepository<Rate, Integer> {
    @Query("SELECT AVG(r.rate_value) FROM Rate r WHERE r.post.id =:postId")
    Float getAverageRateForPost(@Param("postId") UUID postId);

    @Query("SELECT r from Rate r WHERE r.post.id =:postId AND r.user =:user")
    Optional<Rate> findRateByUserAndPost(@Param("postId") UUID postId, @Param("user") User user);

    @Query("SELECT COUNT(r) FROM Rate r WHERE r.post.id =:postId ")
    Integer countRateByPost(@Param("postId") UUID postId);
}
