package com.fashionflow;

import com.fashionflow.constant.Gender;
import com.fashionflow.entity.Member;
import com.fashionflow.repository.MemberRepository;
import groovy.util.logging.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
//@Transactional
class ShopApplicationTests {

    @Autowired
    private MemberRepository memberRepository;

    public Member createMember(){

        //LocalDateTime으로 변환하기 위해 임시로 생성한 생일 데이터
        LocalDateTime birthday = LocalDateTime.of(1998, 11, 2,0,0);

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