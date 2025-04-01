package com.hiutaleapp.service;

import com.hiutaleapp.dto.CategoryDTOFA;
import com.hiutaleapp.entity.Category;
import com.hiutaleapp.entity.CategoryFA;
import com.hiutaleapp.repository.CategoryFARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceFA {
    @Autowired
    private CategoryFARepository categoryRepository;

    public List<CategoryDTOFA> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<CategoryDTOFA> getCategoryById(Long id) {
        return categoryRepository.findById(id).map(this::mapToDTO);
    }

    public CategoryDTOFA createCategory(CategoryFA category) {
        return mapToDTO(categoryRepository.save(category));
    }

    public CategoryDTOFA updateCategory(Long id, CategoryFA category) {
        category.setCategoryId(id);
        return mapToDTO(categoryRepository.save(category));
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    public CategoryDTOFA mapToDTO(CategoryFA category) {
        return new CategoryDTOFA(category);
    }

    public Category mapToEntity(CategoryDTOFA categoryDTO) {
        Category category = new Category();
        category.setCategoryId(categoryDTO.getCategoryId());
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        category.setCreatedAt(categoryDTO.getCreatedAt());
        category.setUpdatedAt(categoryDTO.getUpdatedAt());
        return category;
    }
}