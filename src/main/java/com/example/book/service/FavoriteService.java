package com.example.book.service;

import com.example.book.domain.BookDetails;
import com.example.book.entity.Book;
import com.example.book.entity.Favorite;
import com.example.book.entity.User;
import com.example.book.entity.UserHistory;
import com.example.book.repository.BookRepository;
import com.example.book.repository.FavoriteRepository;
import com.example.book.repository.UserHistoryRepository;
import com.example.book.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final ModelMapper mapper;
    private final UserRepository userRepository;
    private final FavoriteRepository favoriteRepository;
    private final BookRepository bookRepository;
    private final UserHistoryRepository userHistoryRepository;

    public Set<BookDetails> viewBooksInFavorite(){
        Favorite favorite = checkUserFavoriteIfExistForCurrentUser();
        return favorite.getBooks()
                .stream()
                .map(book -> mapper.map(book, BookDetails.class))
                .collect(Collectors.toSet());
    }

    public Favorite addBookToFavorite(Integer book_id){
        Favorite favorite = checkUserFavoriteIfExistForCurrentUser();
        Book book = bookRepository.findById(book_id).orElseThrow();
        favorite.getBooks().add(book);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User current_user = userRepository.findUserByEmail(auth.getName()).get();
        // Add new user action to user history
        UserHistory userHistory = new UserHistory(current_user, LocalDateTime.now(),"add a book to their favorite");
        userHistoryRepository.save(userHistory);
        return favoriteRepository.save(favorite);
    }

    public Favorite removeBookFromFavorite(Integer book_id){
        Favorite favorite = checkUserFavoriteIfExistForCurrentUser();
        Book book = bookRepository.findById(book_id).orElseThrow();
        favorite.getBooks().remove(book);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User current_user = userRepository.findUserByEmail(auth.getName()).get();
        // Add new user action to user history
        UserHistory userHistory = new UserHistory(current_user, LocalDateTime.now(),"remove a book from their favorite");
        userHistoryRepository.save(userHistory);
        return favoriteRepository.save(favorite);
    }

    public Favorite checkUserFavoriteIfExistForCurrentUser(){
        Favorite favorite;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findUserByEmail(auth.getName()).get();
        Optional<Favorite> existing_favorite = favoriteRepository.findFavoriteByUser(user);
        if (existing_favorite.isEmpty()){
            System.out.println("There are no favorite for this user");
            favorite = new Favorite(user);
            favorite.setId(UUID.randomUUID());
            favoriteRepository.save(favorite);
        }
        else {
            System.out.println("Existing favorite found");
            favorite = existing_favorite.get();
        }
        return favorite;
    }
}
