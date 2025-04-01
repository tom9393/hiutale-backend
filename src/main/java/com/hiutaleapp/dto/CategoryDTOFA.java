package com.hiutaleapp.dto;

import com.hiutaleapp.entity.CategoryFA;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class CategoryDTOFA {
    private Long categoryId;
    private String name;
    private String description;
    private Date createdAt;
    private Date updatedAt;

    public CategoryDTOFA(CategoryFA category) {
        this.categoryId = category.getCategoryId();
        this.name = category.getName();
        this.description = category.getDescription();
        this.createdAt = category.getCreatedAt();
        this.updatedAt = category.getUpdatedAt();
    }
}