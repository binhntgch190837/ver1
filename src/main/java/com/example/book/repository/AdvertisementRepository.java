package com.example.book.repository;

import com.example.book.entity.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Integer> {
    @Query("SELECT a from Advertisement a WHERE a.link_url =:link_url")
    Optional<Advertisement> findAdvertisementByURL(@Param("link_url") String link_url);

    @Query("SELECT a from Advertisement a WHERE a.status =:status")
    List<Advertisement> selectEnabledAdvertises(@Param("status") String status);
}
