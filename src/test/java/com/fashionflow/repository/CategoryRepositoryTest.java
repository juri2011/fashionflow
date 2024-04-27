package com.fashionflow.repository;

import com.fashionflow.entity.Category;
import com.fashionflow.entity.Item;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CategoryRepositoryTest {
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ItemRepository itemRepository;

    @Test
    //아이디로 카테고리 찾기
    public void findCategoryByIdTest(){
        Item item = itemRepository.findById(1L).orElseThrow(() ->
                new EntityNotFoundException("해당 상품이 존재하지 않습니다. id = " + 1L)); //1번 상품 찾기

        Category category = categoryRepository.findById(item.getCategory().getId()).orElseThrow(() ->
                new EntityNotFoundException("카테고리가 존재하지 않습니다."));

        Category parentCategory = null;
        if(category.getParent() != null){
            parentCategory = categoryRepository.findById(category.getParent().getId()).orElseThrow(() ->
                    new EntityNotFoundException("카테고리가 존재하지 않습니다."));
        }

        System.out.println("=============================== 하위카테고리 : "+category);
        System.out.println("=============================== 상위카테고리 : "+parentCategory);
    }
}