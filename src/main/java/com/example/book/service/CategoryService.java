package com.example.book.service;

import com.example.book.domain.CategoryHandling;
import com.example.book.entity.Category;
import com.example.book.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final ModelMapper mapper;
    private final CategoryRepository categoryRepository;

    public void showCategoryList(Model model){
        List<Category> categories = categoryRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        model.addAttribute("categories",categories);
    }

    public void saveCategory(CategoryHandling categoryHandling){
        Category category = mapper.map(categoryHandling, Category.class);
        categoryRepository.save(category);
    }

    public List<String> getCategories(){
        return categoryRepository.findAll().stream().map(Category::getName).toList();
    }

}
