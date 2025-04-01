package com.hiutaleapp.service;

import com.hiutaleapp.dto.CategoryDTOFI;
import com.hiutaleapp.entity.Category;
import com.hiutaleapp.entity.CategoryFI;
import com.hiutaleapp.repository.CategoryFIRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceFI {
    @Autowired
    private CategoryFIRepository categoryRepository;

    public List<CategoryDTOFI> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<CategoryDTOFI> getCategoryById(Long id) {
        return categoryRepository.findById(id).map(this::mapToDTO);
    }

    public CategoryDTOFI createCategory(CategoryFI category) {
        return mapToDTO(categoryRepository.save(category));
    }

    public CategoryDTOFI updateCategory(Long id, CategoryFI category) {
        category.setCategoryId(id);
        return mapToDTO(categoryRepository.save(category));
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    public CategoryDTOFI mapToDTO(CategoryFI category) {
        return new CategoryDTOFI(category);
    }

    public Category mapToEntity(CategoryDTOFI categoryDTO) {
        Category category = new Category();
        category.setCategoryId(categoryDTO.getCategoryId());
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        category.setCreatedAt(categoryDTO.getCreatedAt());
        category.setUpdatedAt(categoryDTO.getUpdatedAt());
        return category;
    }
}