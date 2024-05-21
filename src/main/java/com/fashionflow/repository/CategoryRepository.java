package com.fashionflow.repository;

import com.fashionflow.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;

// JpaRepository 또는 CrudRepository를 확장하여 Category 엔터티에 대한 데이터 액세스를 처리하는 리포지토리
public interface CategoryRepository extends CrudRepository<Category, Long> {

    // 코드에 해당하는 카테고리를 찾는 메서드
    public Category findByCode(String code);

    // 특정 부모 ID에 해당하는 하위 카테고리를 찾는 메서드
    List<Category> findByParentId(Long parentId);

    // 부모가 없는(루트) 카테고리를 찾는 메서드
    List<Category> findByParentIsNull();
}
