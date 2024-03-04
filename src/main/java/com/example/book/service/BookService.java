package com.example.book.service;

import com.example.book.domain.BookDetails;
import com.example.book.domain.BookHandling;
import com.example.book.entity.Book;
import com.example.book.entity.Category;
import com.example.book.entity.User;
import com.example.book.repository.BookRepository;
import com.example.book.repository.CategoryRepository;
import com.example.book.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {
    private final ModelMapper mapper;
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CategoryService categoryService;

    public List<BookDetails> getFilteredBooks(Category category, LocalDate startDate, LocalDate endDate, Integer recommended_age){
        List<Book> filtered_books = bookRepository.filterBook(category,startDate,endDate,recommended_age);
        List<BookDetails> books = new ArrayList<>();
        return bookResults(filtered_books,books);
    }

    public List<BookDetails> getTitleSearchedBooks(String title){
        List<Book> searched_books = bookRepository.searchBookByTitle(title);
        List<BookDetails> books = new ArrayList<>();
        return bookResults(searched_books,books);
    }

    public List<BookDetails> getAuthorSearchedBooks(String author){
        List<Book> searched_books = bookRepository.searchBookByAuthor(author);
        List<BookDetails> books = new ArrayList<>();
        return bookResults(searched_books,books);
    }

    public List<BookDetails> bookResults(List<Book> books, List<BookDetails> mappedBooks){
        for (Book book : books){
            BookDetails bookDetails = mapper.map(book, BookDetails.class);
            mappedBooks.add(bookDetails);
        }
        return mappedBooks;
    }

    public void configureBookBeforeAdding(BookHandling bookHandling){
        // Convert date from string
        String published_date = bookHandling.getPublished_day();
        LocalDate date = LocalDate.parse(published_date.trim(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        // Map book entity and save to database
        Book book = mapper.map(bookHandling, Book.class);
        book.setCategory(categoryRepository.findCategoryByName(bookHandling.getCategory_name()).get());
        book.setRecommended_age(Integer.parseInt(bookHandling.getRecommended_age()));
        book.setPublished_date(date);
        bookRepository.save(book);
    }

    public BookHandling configureBookBeforeEditing(Integer book_id){
        Optional<Book> book = Optional.of(bookRepository.findById(book_id).orElseThrow());
        return mapper.map(book.get(), BookHandling.class);
    }

    public void configureBookDetailForAdmin(BookDetails book_details, Model model){
        String book_category = book_details.getCategory().getName();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String published_date = book_details.getPublished_date().format(formatter);
        String recommended_age = Integer.toString(book_details.getRecommended_age());
        String page = Integer.toString(book_details.getPage());
        model.addAttribute("book_category",book_category);
        model.addAttribute("published_date",published_date);
        model.addAttribute("recommended_age",recommended_age);
        model.addAttribute("page",page);
    }

    public void configureBookDetailForUser(BookDetails bookDetails){
        Book book = bookRepository.findById(bookDetails.getId()).orElseThrow();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        bookDetails.setPublished_day(book.getPublished_date().format(formatter));
        bookDetails.setCategory_name(book.getCategory().getName());
    }

    public void configureBookWhileEditing(BookHandling bookHandling){
        // Convert date from string
        String published_date = bookHandling.getPublished_day();
        LocalDate date = LocalDate.parse(published_date.trim(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        // Map book entity and save to database
        Optional<Book> existing_book = bookRepository.findById(bookHandling.getId());
        if(existing_book.isPresent()){
            Book book = mapper.map(bookHandling, Book.class);
            book.setCategory(categoryRepository.findCategoryByName(bookHandling.getCategory_name()).get());
            book.setRecommended_age(Integer.parseInt(bookHandling.getRecommended_age()));
            book.setPublished_date(date);
            bookRepository.save(book);
        }
    }

    public void deleteBook(Integer book_id){
        Optional<Book> book = Optional.of(bookRepository.findById(book_id).orElseThrow());
        bookRepository.delete(book.get());
    }

    public List<BookDetails> getRecommendedBooksForUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findUserByEmail(auth.getName()).get();
        List<Book> recommended_books = bookRepository.findRelatedBooksByUserFavorites(user.getId());
        // Remove excess records if more than 7
        if (recommended_books.size() > 7) {
            recommended_books = recommended_books.subList(0, 7);
        }
        // Add random books if less than 7
        else if (recommended_books.size() < 7) {
            int remaining = 7 - recommended_books.size();

            // Fetch all books except the related ones
            List<Book> allBooks = bookRepository.findAll();
            allBooks.removeAll(recommended_books);

            // Shuffle the books randomly
            Collections.shuffle(allBooks);

            // Add remaining random books to the recommended_books list
            recommended_books.addAll(allBooks.subList(0, remaining));
        }
        return recommended_books.stream().map(book -> mapper.map(book, BookDetails.class)).toList();
    }

    public BookDetails getBookDetails(Integer book_id){
        Optional<Book> book = Optional.of(bookRepository.findById(book_id).orElseThrow());
        return mapper.map(book.get(), BookDetails.class);
    }

    public List<String> getAgeToString(List<Integer> ages){
        return ages.stream().map(Objects::toString).collect(Collectors.toList());
    }

    public List<Integer> getAge(){
        List<Integer> ages = new ArrayList<>();
        ages.add(6);
        ages.add(12);
        ages.add(16);
        return ages;
    }

    public void updateBookInfoModel(Model model){
        List<String> ages = getAgeToString(getAge());
        model.addAttribute("categories", categoryService.getCategories());
        model.addAttribute("ages", ages);
    }
}
