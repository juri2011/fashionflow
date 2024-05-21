package com.fashionflow.service;

import com.fashionflow.dto.ItemFormDTO;
import com.fashionflow.dto.ReportItemDTO;
import com.fashionflow.entity.Item;
import com.fashionflow.entity.Member;
import com.fashionflow.entity.ReportItem;
import com.fashionflow.repository.ItemRepository;
import com.fashionflow.repository.MemberRepository;
import com.fashionflow.repository.ReportItemRepository;
import com.fashionflow.repository.ReportItemTagRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ReportItemServiceTest {

    @Autowired
    ReportItemRepository reportItemRepository;

    @Autowired
    ReportItemTagRepository reportItemTagRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    /**
     * 모든 신고 항목을 조회하여 콘솔에 출력하는 테스트 메서드.
     * 신고 항목을 등록일자 역순으로 정렬하여 출력.
     */
    @Test
    public void findReportTest() {
        // 모든 신고 항목을 등록일자 역순으로 조회
        List<ReportItem> reportItemList = reportItemRepository.findAllByOrderByRegdateDesc();
        // 각 신고 항목을 콘솔에 출력
        reportItemList.forEach(reportItem -> System.out.println("==============================" + reportItem));
    }

    /**
     * 새로운 신고 항목을 생성하고 데이터베이스에 저장하는 테스트 메서드.
     * 여러 개의 신고 항목을 생성하고 저장된 항목이 올바른지 검증.
     */
    @Test
    @Commit
    public void createReportItem() {
        Long itemId = 2L; // 신고할 상품의 ID
        // 상품 정보 조회 및 DTO 생성
        ItemFormDTO itemFormDTO = ItemFormDTO.of(itemRepository.findById(itemId).orElse(null));
        // DTO를 통해 상품 엔티티 생성
        Item item = itemFormDTO.createItem();

        // 신고하는 회원 정보 조회
        Member member = memberRepository.findByEmail("test1@test.com");

        for (int i = 0; i < 5; i++) {
            // 신고 항목 DTO 생성
            ReportItemDTO reportItemDTO = ReportItemDTO.builder()
                    .reporterMemberEmail("test1@test.com")
                    .reportedItemId(itemFormDTO.getId())
                    .reportedItem(itemFormDTO)
                    .regdate(LocalDateTime.now())
                    .content("신고합니다")
                    .build();

            // 신고 항목 엔티티 생성
            ReportItem reportItem = reportItemDTO.createReportItem();
            // 신고 항목에 회원과 상품 정보 설정
            reportItem.setReporterMember(member);
            reportItem.setReportedItem(item);

            // 신고 항목을 데이터베이스에 저장
            ReportItem savedReportItem = reportItemRepository.save(reportItem);
            // 저장된 신고 항목이 올바른지 검증
            assertEquals(reportItem, savedReportItem);
        }
    }

    /**
     * 페이징 처리가 적용된 신고 항목 리스트를 조회하여 콘솔에 출력하는 테스트 메서드.
     * 특정 페이지의 신고 항목을 조회하고 페이지 수를 출력.
     */
    @Test
    public void findReportTest2() {
        // 2페이지의 두 개 항목을 조회하는 페이지 요청 생성 (0페이지부터 시작)
        Pageable pageable = PageRequest.of(0, 2);
        // 페이징 처리가 적용된 신고 항목 리스트 조회
        Page<ReportItem> reportItems = reportItemRepository.findAll(pageable);

        // 각 신고 항목을 콘솔에 출력
        reportItems.getContent()
                .forEach(reportItem -> System.out.println("================ id : " + reportItem.getId() + "\n"));

        // 총 페이지 수를 콘솔에 출력
        System.out.println("페이지수? :" + reportItems.getTotalPages());
    }
}