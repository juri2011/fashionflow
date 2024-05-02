package com.fashionflow.repository;

import com.fashionflow.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;

public interface CategoryRepository extends CrudRepository<Category, Long> {

    public Category findByCode(String code);

    // 부모 ID에 해당하는 하위 카테고리를 찾는 메서드
    List<Category> findByParentId(Long parentId);

    List<Category> findByParentIsNull();
}
