package com.hiutaleapp.service;

import com.hiutaleapp.dto.CategoryDTOJA;
import com.hiutaleapp.entity.Category;
import com.hiutaleapp.entity.CategoryJA;
import com.hiutaleapp.repository.CategoryJARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceJA {
    @Autowired
    private CategoryJARepository categoryRepository;

    public List<CategoryDTOJA> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<CategoryDTOJA> getCategoryById(Long id) {
        return categoryRepository.findById(id).map(this::mapToDTO);
    }

    public CategoryDTOJA createCategory(CategoryJA category) {
        return mapToDTO(categoryRepository.save(category));
    }

    public CategoryDTOJA updateCategory(Long id, CategoryJA category) {
        category.setCategoryId(id);
        return mapToDTO(categoryRepository.save(category));
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    public CategoryDTOJA mapToDTO(CategoryJA category) {
        return new CategoryDTOJA(category);
    }

    public Category mapToEntity(CategoryDTOJA categoryDTO) {
        Category category = new Category();
        category.setCategoryId(categoryDTO.getCategoryId());
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        category.setCreatedAt(categoryDTO.getCreatedAt());
        category.setUpdatedAt(categoryDTO.getUpdatedAt());
        return category;
    }
}