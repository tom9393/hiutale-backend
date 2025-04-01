package com.hiutaleapp.repository;

import com.hiutaleapp.entity.CategoryFI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryFIRepository extends JpaRepository<CategoryFI, Long> {}
