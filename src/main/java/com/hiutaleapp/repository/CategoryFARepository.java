package com.hiutaleapp.repository;

import com.hiutaleapp.entity.CategoryFA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryFARepository extends JpaRepository<CategoryFA, Long> {}
