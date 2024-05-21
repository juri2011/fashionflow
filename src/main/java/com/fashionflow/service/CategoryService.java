package com.fashionflow.service;

import com.fashionflow.dto.CategoryDTO;
import com.fashionflow.entity.Category;
import com.fashionflow.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // 모든 카테고리를 조회하여 DTO 리스트 반환
    public List<CategoryDTO> findAllCategories() {
        // 데이터베이스에서 모든 카테고리를 조회합니다.
        List<Category> categories = (List<Category>) categoryRepository.findAll();

        // 조회된 카테고리 엔티티 리스트를 CategoryDTO 리스트로 변환합니다.
        return categories.stream()
                .map(CategoryDTO::entityToDto)
                .collect(Collectors.toList());
    }

    // 부모 카테고리를 조회하여 DTO 리스트 반환
    public List<CategoryDTO> findParentCategories() {
        List<Category> parents = categoryRepository.findByParentIsNull();
        return parents.stream()
                .map(CategoryDTO::entityToDto)
                .collect(Collectors.toList());
    }

    // 지정된 부모 카테고리 ID의 서브 카테고리를 조회하여 DTO 리스트 반환
    public List<CategoryDTO> findSubcategoriesByParentId(Long parentId) {
        List<Category> subcategories = categoryRepository.findByParentId(parentId);
        return subcategories.stream()
                .map(CategoryDTO::entityToDto)
                .collect(Collectors.toList());
    }
}
