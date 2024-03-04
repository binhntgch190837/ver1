package com.example.book.repository;

import com.example.book.entity.User;
import com.example.book.entity.UserHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserHistoryRepository extends JpaRepository<UserHistory, Integer> {
    @Query("Select uh from UserHistory uh WHERE uh.user =:user")
    List<UserHistory> getHistoryByUser(@Param("user") User user);
}
