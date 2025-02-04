package com.hiutaleapp.dto;

import com.hiutaleapp.entity.Category;
import com.hiutaleapp.repository.CategoryRepository;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class CategoryDTO {
    private Long categoryId;
    private String name;
    private String description;
    private Date createdAt;
    private Date updatedAt;

    public CategoryDTO(Category category) {
        this.categoryId = category.getCategoryId();
        this.name = category.getName();
        this.description = category.getDescription();
        this.createdAt = category.getCreatedAt();
        this.updatedAt = category.getUpdatedAt();
    }
}