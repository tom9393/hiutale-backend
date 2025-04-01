package com.hiutaleapp.controller;

import com.hiutaleapp.dto.CategoryDTO;
import com.hiutaleapp.dto.CategoryDTOFA;
import com.hiutaleapp.dto.CategoryDTOFI;
import com.hiutaleapp.dto.CategoryDTOJA;
import com.hiutaleapp.entity.Category;
import com.hiutaleapp.service.CategoryService;
import com.hiutaleapp.service.CategoryServiceFA;
import com.hiutaleapp.service.CategoryServiceFI;
import com.hiutaleapp.service.CategoryServiceJA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryServiceFI categoryServiceFI;
    @Autowired
    private CategoryServiceJA categoryServiceJA;
    @Autowired
    private CategoryServiceFA categoryServiceFA;

    @GetMapping("en/all")
    public List<CategoryDTO> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("fi/all")
    public List<CategoryDTOFI> getAllCategoriesFI() {
        return categoryServiceFI.getAllCategories();
    }

    @GetMapping("ja/all")
    public List<CategoryDTOJA> getAllCategoriesJA() {
        return categoryServiceJA.getAllCategories();
    }

    @GetMapping("fa/all")
    public List<CategoryDTOFA> getAllCategoriesFA() {
        return categoryServiceFA.getAllCategories();
    }

    @GetMapping("/one/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public CategoryDTO createCategory(@RequestBody Category category) {
        return categoryService.createCategory(category);
    }

    @PutMapping("/update/{id}")
    public CategoryDTO updateCategory(@PathVariable Long id, @RequestBody Category category) {
        return categoryService.updateCategory(id, category);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }
}