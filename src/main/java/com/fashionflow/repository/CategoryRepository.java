package com.fashionflow.repository;

import com.fashionflow.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Long> {

    public Category findByCode(String code);
}