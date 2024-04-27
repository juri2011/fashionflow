package com.fashionflow;

import com.fashionflow.constant.Gender;
import com.fashionflow.constant.ItemStatus;
import com.fashionflow.constant.SellStatus;
import com.fashionflow.entity.Category;
import com.fashionflow.entity.Item;
import com.fashionflow.entity.Member;
import com.fashionflow.entity.QCategory;
import com.fashionflow.repository.CategoryRepository;
import com.fashionflow.repository.ItemRepository;
import com.fashionflow.repository.MemberRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import groovy.util.logging.Slf4j;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
//@Transactional
class ShopApplicationTests {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    EntityManager em;

    public Member createMember(){

        //LocalDateTime으로 변환하기 위해 임시로 생성한 생일 데이터
        LocalDate birthday = LocalDate.of(1998, 11, 2);

        /*
            여기서는 builder를 통해 임시로 테스트를 진행했지만
            다음에 member 테스트를 할 때는 MemberFormDto 클래스와 Member 엔티티 안에 createMember 메소드를 생성하고
            createMember() 메소드에 memberFormDto 형의 파라미터를 넘겨서 생성하는 것이 좋겠습니다
        */
        Member member = Member.builder()
                .name("오주리")
                .email("juri@test.com")
                .pwd("12345")
                .nickname("주리")
                .phone("010-2000-8000")
                .birth(birthday)
                .gender(Gender.f)
                .mannerScore(50)
                .userStnum("12345")
                .userAddr("경기도 ㅇㅇ시 ㅇㅇ구 ㅇㅇ로 123")
                .userDaddr("oo아파트 100동 101호")
                .regdate(LocalDateTime.now())
                .build();

        //아직 memberRepository로 save되지 않았기 때문에 id는 null인 상태이다.
        System.out.println("=================================="+member);

        return member;
    }


    public Item createItem(){
        
        //데이터 한 개 정상출력되는지 확인하기 위해 더미데이터 한 개 생성

        Category category = findCategory();
        Item item = Item.builder()
                .itemName("알토 봄 청자켓 연청 데님자켓")
                .content("사이즈 프리사이즈 44~55 여유있게 입는 자켓핏 아니고 정사이즈로 입는 핏이에요 66은 x 정핏에 허리까지 오는 기장감이 날씬해보이고 다리 길어보여요 이쁜 연청이라 얼굴 화사해요~^^ 일반4천 반값2천택배")
                .price(7000)
                .delivery(4000)
                .address("경기도 용인시 기흥구")
                .regdate(LocalDateTime.now())
                .category(category)
                .itemStatus(ItemStatus.NO_SIGNS_OF_USE)
                .sellStatus(SellStatus.SELLING)
                .build();
        return item;

    }
    public Category findCategory(){
        /*

        @Transactional 어노테이션 사용 시 작동됨
        Category category = categoryRepository.findByCode("WOMEN_OUTER");
        System.out.println("==========================="+category);
        */

        //QueryDSL을 사용하는 방법
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QCategory qCategory = QCategory.category;

        Category category = queryFactory.select(qCategory)
                .from(qCategory)
                .where(qCategory.code.like("WOMEN_OUTER"))
                .fetchOne();

        System.out.println("======================="+category);
        assertNotNull(category);

        return category;
    }

    @Test
    public void findItemTest(){
        Item item = createItem();

        Item savedItem = itemRepository.save(item);

        System.out.println("========================== 검색결과: "+savedItem);

        assertEquals(item.getId(), savedItem.getId());
    }
    @Test
    public void findMemberTest(){

        //바로 위에 있는 메소드 실행
        Member member = createMember();

        //실제로 DB에 저장
        Member savedMember = memberRepository.save(member);

        //member객체의 이메일을 이용하여 같은 이메일을 가지고 있는 데이터 검색
        Member findMember = memberRepository.findByEmail(member.getEmail());

        //결과적으로는 같은 데이터가 출력되지만 findByEmail이 정상적으로 작동되는 것을 확인
        System.out.println("=================================== 검색결과 : " + findMember);

        //두 객체의 id값을 비교해서 데이터가 정상적으로 삽입되었는지 확인
        assertEquals(savedMember.getId(), member.getId());
    }
}