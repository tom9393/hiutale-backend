package com.hiutaleapp.repository;

import com.hiutaleapp.entity.CategoryJA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryJARepository extends JpaRepository<CategoryJA, Long> {}
