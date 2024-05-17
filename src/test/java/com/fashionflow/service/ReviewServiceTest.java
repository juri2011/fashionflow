package com.fashionflow.service;

import com.fashionflow.entity.Item;
import com.fashionflow.entity.Review;
import com.fashionflow.repository.ItemRepository;
import com.fashionflow.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ReviewServiceTest {

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    ItemRepository itemRepository;

    @Test
    public void findReviewBySellerEmail(){

        String userEmail = "dhwnfl13@gmail.com";

        List<Review> reviews = reviewRepository.findAll().stream().filter(review -> {
            Optional<Item> item = itemRepository.findById(review.getItemId());
            return item.isPresent() && item.get().getMember().getEmail().equals(userEmail);
        }).toList();

        reviews.forEach(review -> System.out.println("==========================" + review.getMemberEmail()));
    }
}