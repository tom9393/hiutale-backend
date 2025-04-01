package com.hiutaleapp.dto;

import com.hiutaleapp.entity.CategoryJA;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class CategoryDTOJA {
    private Long categoryId;
    private String name;
    private String description;
    private Date createdAt;
    private Date updatedAt;

    public CategoryDTOJA(CategoryJA category) {
        this.categoryId = category.getCategoryId();
        this.name = category.getName();
        this.description = category.getDescription();
        this.createdAt = category.getCreatedAt();
        this.updatedAt = category.getUpdatedAt();
    }
}