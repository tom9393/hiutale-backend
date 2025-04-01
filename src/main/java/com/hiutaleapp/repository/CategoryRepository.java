package com.hiutaleapp.repository;

import com.hiutaleapp.entity.Category;
import com.hiutaleapp.entity.CategoryFA;
import com.hiutaleapp.entity.CategoryFI;
import com.hiutaleapp.entity.CategoryJA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {}
