package com.example.book.controller;

import com.example.book.domain.BookDetails;
import com.example.book.entity.Favorite;
import com.example.book.service.AdvertisementService;
import com.example.book.service.BookService;
import com.example.book.service.FavoriteService;
import com.example.book.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Set;

@Controller
@RequiredArgsConstructor
public class FavoriteController {
    private final UserService userService;
    private final BookService bookService;
    private final FavoriteService favoriteService;
    private final AdvertisementService advertisementService;

    // View books in favorite for user
    @RequestMapping(path = "/user/favorite", method = RequestMethod.GET)
    public String viewBookInFavorite(Model model){
        userService.updateModel(model);
        advertisementService.getEnabledAdvertisements(model);
        Set<BookDetails> books = favoriteService.viewBooksInFavorite();
        books.forEach(bookService::configureBookDetailForUser);
        model.addAttribute("books", books);
        return "user/favorite";
    }

    // Add book to favorite for user
    @RequestMapping(path = "/user/add-book-to-favorite/{id}", method = RequestMethod.GET)
    public String addBookToFavorite(@PathVariable("id") Integer book_id){
        Favorite favorite = favoriteService.addBookToFavorite(book_id);
        return "redirect:/user/favorite";
    }

    // Remove book from favorite for user
    @RequestMapping(path = "/user/remove-favorite/{id}", method = RequestMethod.GET)
    public String removeFromFavorite(@PathVariable("id") Integer book_id){
        Favorite favorite = favoriteService.removeBookFromFavorite(book_id);
        return "redirect:/user/favorite";
    }
}
