package com.example.book.repository;

import com.example.book.entity.Book;
import com.example.book.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    @Query("SELECT b from Book b WHERE (b.category =:category)")
    List<Book> findBookByCategory(@Param("category") Category category);

    @Query("SELECT b from Book b WHERE b.title =:title")
    Optional<Book> findBookByTitle(@Param("title") String title);

    @Query("SELECT b from Book b WHERE (b.title LIKE CONCAT('%', COALESCE(:title, ''), '%')" +
            "OR :title IS NULL)")
    List<Book> searchBookByTitle(@Param("title") String title);

    @Query("SELECT b from Book b WHERE (b.author LIKE CONCAT('%', COALESCE(:author, ''), '%')" +
            "OR :author IS NULL)")
    List<Book> searchBookByAuthor(@Param("author") String author);

    @Query("SELECT b from Book b WHERE (:category is null OR b.category = :category) " +
            "AND (((cast(:startDate as LocalDate) is null ) and (cast(:endDate as LocalDate) is null )) OR (CAST(b.published_date AS LocalDate) >= :startDate AND CAST(b.published_date AS LocalDate) <= :endDate))" +
            "AND (:recommended_age is null OR b.recommended_age >= :recommended_age)")
    List<Book> filterBook(@Param("category") Category category,
                          @Param("startDate") LocalDate startDate,
                          @Param("endDate") LocalDate endDate,
                          @Param("recommended_age") Integer recommended_age);

    @Query("SELECT b2 FROM User u " +
            "JOIN u.favorites f " +
            "JOIN f.books fb " +
            "JOIN fb.category c " +
            "JOIN c.books b1 " +
            "JOIN b1.category c2 " +
            "JOIN c2.books b2 " +
            "WHERE u.id = :userId " +
            "AND b2.id != b1.id " +
            "AND b2.id NOT IN " +
            "(SELECT fb2.id FROM Favorite f2 " +
            "JOIN f2.books fb2 " +
            "JOIN fb2.category c3 " +
            "WHERE f2.id = f.id) " +
            "AND b2.id = (" +
            "SELECT MIN(b3.id) FROM Book b3 " +
            "JOIN b3.category c4 " +
            "WHERE c4.id = c2.id" +
            ")")
    List<Book> findRelatedBooksByUserFavorites(@Param("userId") UUID userId);

}
