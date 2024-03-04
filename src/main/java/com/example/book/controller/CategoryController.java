package com.example.book.controller;

import com.example.book.domain.CategoryHandling;
import com.example.book.entity.Book;
import com.example.book.entity.Category;
import com.example.book.repository.BookRepository;
import com.example.book.repository.CategoryRepository;
import com.example.book.service.CategoryService;
import com.example.book.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;


@Controller
@RequiredArgsConstructor
public class CategoryController {
    private final ModelMapper mapper;
    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;
    private final UserService userService;
    private final CategoryService categoryService;

    @RequestMapping(path = "/admin/category-index", method = RequestMethod.GET)
    public String viewCategory(Model model){
        userService.updateModel(model);
        categoryService.showCategoryList(model);
        return "admin/category_index";
    }

    @RequestMapping(path = "/admin/add-category", method = RequestMethod.GET)
    public String addCategory(Model model){
        userService.updateModel(model);
        model.addAttribute("category", new CategoryHandling());
        return "admin/add_category";
    }

    @RequestMapping(path = "/admin/add-category", method = RequestMethod.POST)
    public String addCategoryForm(@ModelAttribute("category") @Valid CategoryHandling categoryHandling,
                                  BindingResult result, Model model){
        if(result.hasErrors()){
            userService.updateModel(model);
            return "admin/add_category";
        }
        else if (categoryRepository.findCategoryByName(categoryHandling.getName()).isPresent()) {
            userService.updateModel(model);
            result.rejectValue("name",null,"Category name already exists.");
            return "admin/add_category";
        }
        else {
            categoryService.saveCategory(categoryHandling);
            return "redirect:/admin/category-index";
        }
    }

    @RequestMapping(path = "/admin/edit-category/{id}", method = RequestMethod.GET)
    public String editCategory(@PathVariable("id") int category_id, Model model, RedirectAttributes redirectAttributes){
        userService.updateModel(model);
        Optional<Category> category = Optional.of(categoryRepository.findById(category_id).orElseThrow());
        String category_name = category.get().getName();
        System.out.println("Category: " + category_name);
        List<Book> books = bookRepository.findBookByCategory(category.get());
        if(books.size() == 0){
            System.out.println("The books of this category is empty.");
            CategoryHandling categoryHandling = mapper.map(category.get(), CategoryHandling.class);
            model.addAttribute("edit_category", categoryHandling);
            return "admin/edit_category";
        }
        else {
            System.out.println("The book size: " + books.size());
            redirectAttributes.addFlashAttribute("message","Category " + category_name + " contains its books so it is unable to be edited.");
            return "redirect:/admin/category-index";
        }
    }

    @RequestMapping(path = "/admin/edit-category", method = RequestMethod.POST)
    public String editCategoryForm(@ModelAttribute("edit_category") @Valid CategoryHandling categoryHandling,
                                   BindingResult result, Model model){
        if(result.hasErrors()){
            userService.updateModel(model);
            return "admin/edit_category";
        }
        else if (categoryRepository.findCategoryByName(categoryHandling.getName()).isPresent()) {
            userService.updateModel(model);
            result.rejectValue("name",null,"Category name already exists.");
            return "admin/edit_category";
        }
        else{
            categoryService.saveCategory(categoryHandling);
            return "redirect:/admin/category-index";
        }
    }

    @RequestMapping(path = "/admin/delete-category/{id}", method = RequestMethod.GET)
    public String deleteCategory(@PathVariable("id") int category_id, Model model, RedirectAttributes redirectAttributes){
        userService.updateModel(model);
        Optional<Category> category = Optional.of(categoryRepository.findById(category_id).orElseThrow());
        String category_name = category.get().getName();
        System.out.println("Category: " + category_name);
        List<Book> books = bookRepository.findBookByCategory(category.get());
        if(books.size() == 0){
            System.out.println("The books of this category is empty.");
            categoryRepository.delete(category.get());
        }
        else {
            System.out.println("The book size: " + books.size());
            redirectAttributes.addFlashAttribute("message","Category " + category_name + " contains its books so it is unable to be deleted.");
        }
        return "redirect:/admin/category-index";
    }
}
