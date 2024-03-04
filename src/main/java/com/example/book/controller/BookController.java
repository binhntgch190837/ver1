package com.example.book.controller;

import com.example.book.domain.BookDetails;
import com.example.book.domain.BookHandling;
import com.example.book.entity.Category;
import com.example.book.repository.BookRepository;
import com.example.book.repository.CategoryRepository;
import com.example.book.service.AdvertisementService;
import com.example.book.service.BookService;
import com.example.book.service.CategoryService;
import com.example.book.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class BookController {
    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;
    private final UserService userService;
    private final BookService bookService;
    private final CategoryService categoryService;
    private final AdvertisementService advertisementService;

    // View books for admin
    @RequestMapping(path = "/admin/book-index", method = RequestMethod.GET)
    public String bookIndex(Model model, @RequestParam(required = false) String title,
                            @RequestParam(required = false) Category category,
                            @RequestParam(required = false) String category_name,
                            @RequestParam(required = false) String author,
                            @RequestParam(required = false) Integer recommended_age,
                            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> startDate,
                            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> endDate){
        userService.updateModel(model);
        List<BookDetails> filtered_books;
        // Filter params
        Optional<Category> searched_category = categoryRepository.findCategoryByName(category_name);
        if(searched_category.isPresent()){
            filtered_books = bookService.getFilteredBooks(searched_category.get(),startDate.orElse(null),endDate.orElse(null),recommended_age);
        }
        else if (title != null && !title.isEmpty()){
            filtered_books = bookService.getTitleSearchedBooks(title);
        }
        else if (author != null && !author.isEmpty()){
            filtered_books = bookService.getAuthorSearchedBooks(author);
        }
        else {
            // Filter by params (default list)
            filtered_books = bookService.getFilteredBooks(category,startDate.orElse(null),endDate.orElse(null),recommended_age);
        }
        model.addAttribute("categories", categoryService.getCategories());
        model.addAttribute("ages", bookService.getAge());
        model.addAttribute("books",filtered_books);
        return "admin/book_index";
    }

    // View books for user
    @RequestMapping(path = "/user/book-index", method = RequestMethod.GET)
    public String bookList(Model model, @RequestParam(required = false) String title,
                            @RequestParam(required = false) Category category,
                            @RequestParam(required = false) String category_name,
                            @RequestParam(required = false) String author,
                            @RequestParam(required = false) Integer recommended_age,
                            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> startDate,
                            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> endDate){
        userService.updateModel(model);
        advertisementService.getEnabledAdvertisements(model);
        List<BookDetails> filtered_books;
        // Filter params
        Optional<Category> searched_category = categoryRepository.findCategoryByName(category_name);
        if(searched_category.isPresent()){
            filtered_books = bookService.getFilteredBooks(searched_category.get(),startDate.orElse(null),endDate.orElse(null),recommended_age);
        }
        else if (title != null && !title.isEmpty()){
            filtered_books = bookService.getTitleSearchedBooks(title);
        }
        else if (author != null && !author.isEmpty()){
            filtered_books = bookService.getAuthorSearchedBooks(author);
        }
        else {
            // Filter by params (default list)
            filtered_books = bookService.getFilteredBooks(category,startDate.orElse(null),endDate.orElse(null),recommended_age);
        }
        filtered_books.forEach(bookService::configureBookDetailForUser);
        model.addAttribute("categories", categoryService.getCategories());
        model.addAttribute("ages", bookService.getAge());
        model.addAttribute("books",filtered_books);
        return "user/book_index";
    }


    @RequestMapping(path = "/admin/book-details/{id}", method = RequestMethod.GET)
    public String bookDetails(@PathVariable("id") Integer book_id, Model model){
        userService.updateModel(model);
        BookDetails book_details = bookService.getBookDetails(book_id);
        // Convert book attributes in front-end
        bookService.configureBookDetailForAdmin(book_details, model);
        model.addAttribute("book",book_details);
        return "admin/book_details";
    }

    @RequestMapping(path = "/admin/add-book", method = RequestMethod.GET)
    public String addBook(Model model){
        userService.updateModel(model);
        bookService.updateBookInfoModel(model);
        model.addAttribute("book", new BookHandling());
        return "admin/add_book";
    }

    @RequestMapping(path = "/admin/add-book", method = RequestMethod.POST)
    public String addBookForm(@ModelAttribute("book") @Valid BookHandling bookHandling, BindingResult result, Model model){
        if(result.hasErrors()){
            userService.updateModel(model);
            bookService.updateBookInfoModel(model);
            return "admin/add_book";
        }
        else if (bookRepository.findBookByTitle(bookHandling.getTitle()).isPresent()){
            userService.updateModel(model);
            bookService.updateBookInfoModel(model);
            result.rejectValue("title",null,"Book title already exist.");
            return "admin/add_book";
        }
        else {
            bookService.configureBookBeforeAdding(bookHandling);
            return "redirect:/admin/book-index";
        }
    }

    @RequestMapping(path = "/admin/edit-book/{id}", method = RequestMethod.GET)
    public String editBook(@PathVariable("id") Integer book_id, Model model){
        userService.updateModel(model);
        bookService.updateBookInfoModel(model);
        BookHandling bookHandling = bookService.configureBookBeforeEditing(book_id);
        model.addAttribute("edit_book", bookHandling);
        return "admin/edit_book";
    }

    @RequestMapping(path = "/admin/edit-book", method = RequestMethod.POST)
    public String editBookForm(@ModelAttribute("edit_book") @Valid BookHandling bookHandling, BindingResult result, Model model){
        if(result.hasErrors()){
            userService.updateModel(model);
            bookService.updateBookInfoModel(model);
            return "admin/edit_book";
        }
        else if (bookRepository.findBookByTitle(bookHandling.getTitle()).isPresent()){
            userService.updateModel(model);
            bookService.updateBookInfoModel(model);
            result.rejectValue("title",null,"Book title already exist.");
            return "admin/edit_book";
        }
        else {
            bookService.configureBookWhileEditing(bookHandling);
            return "redirect:/admin/book-index";
        }
    }

    @RequestMapping(path = "/admin/delete-book/{id}", method = RequestMethod.GET)
    public String deleteBook(@PathVariable("id") Integer book_id){
        bookService.deleteBook(book_id);
        return "redirect:/admin/book-index";
    }
}
