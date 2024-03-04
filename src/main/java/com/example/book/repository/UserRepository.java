package com.example.book.repository;

import com.example.book.entity.Role;
import com.example.book.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    @Query("SELECT u from User u WHERE u.email =:email")
    Optional<User> findUserByEmail(@Param("email") String email);

    @Query("SELECT u from User u WHERE u.id =:id")
    Optional<User> findUserById(@Param("id") UUID user_id);

    @Query("SELECT u from User u WHERE u.username =:username")
    Optional<User> findUserByName(@Param("username") String username);

    @Query("SELECT u from User u WHERE (u.username LIKE CONCAT('%', COALESCE(:username, ''), '%')" +
            "OR :username IS NULL)")
    List<User> searchUserByName(@Param("username") String username);

    @Query("SELECT u from User u WHERE u.phone_number =:phone_number")
    Optional<User> findUserByPhoneNumber(@Param("phone_number") String phone_number);

    @Query("SELECT u from User u WHERE (u.role =:role)")
    List<User> findUserByRole(@Param("role") Role role);

    @Query("SELECT u.friends from User u WHERE u.id =:id")
    List<User> findFriendListByUser(@Param("id") UUID user_id);

    @Query("SELECT u FROM User u WHERE u IN (SELECT f FROM User u JOIN u.friends f WHERE u.id = :id) " +
            "AND (u.username LIKE CONCAT('%', COALESCE(:username, ''), '%') OR :username IS NULL)")
    List<User> searchFriendListByUser(@Param("id") UUID userId, @Param("username") String username);

    @Query("SELECT u FROM User u WHERE u.id <> :userId " +
            "AND u.id NOT IN (SELECT f.id FROM User u JOIN u.friends f WHERE u.id = :userId AND f.id <> :userId)")
    List<User> findNonFriendUsersByUserIdAndLoggedInUserId(@Param("userId") UUID userId);

    @Query("SELECT u FROM User u WHERE u.id <> :userId " +
            "AND u.id NOT IN (SELECT f.id FROM User u JOIN u.friends f WHERE u.id = :userId AND f.id <> :userId)" +
            "AND (u.username LIKE CONCAT('%', COALESCE(:username, ''), '%')" +
            "OR :username IS NULL)")
    List<User> searchNonFriendUsersByUserIdAndLoggedInUserId(@Param("userId") UUID userId, @Param("username") String username);

    @Modifying
    @Query(value = "DELETE FROM user_friends WHERE (user_id = :user_id AND friend_id = :friend_id) " +
            "OR (user_id = :friend_id AND friend_id = :user_id)", nativeQuery = true)
    void deleteFriendshipRelationship(@Param("user_id") UUID user_id, @Param("friend_id") UUID friend_id);
}
