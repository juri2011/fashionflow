package com.fashionflow.service;

import com.fashionflow.constant.Gender;
import com.fashionflow.dto.*;
import com.fashionflow.entity.*;
import com.fashionflow.repository.ItemSellRepository;
import com.fashionflow.repository.MemberRepository;
import com.fashionflow.repository.ProfileImageRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    ProfileImageRepository profileImageRepository;

    @Autowired
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ItemSellRepository itemSellRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Mock
    private SecurityContext securityContext;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void getItemImgTest(){
        /* 회원 상품 리스트 저장 */
        QItem qItem = QItem.item;
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        //현재 상품 번호만 제외하고 등록 순으로 판매자 다른 상품 조회
        List<Item> itemList = queryFactory.select(qItem)
                .from(qItem)
                .where(qItem.member.id.eq(1L))
                .where(qItem.id.ne(1L))
                .orderBy(qItem.id.desc())
                .limit(4)
                .fetch();
        List<ItemFormDTO> itemFormDTOList = new ArrayList<>();
        for(Item item : itemList){
            /* 이미지, 태그 정보는 들어가지 않음 (쓰이지 않음) */
            ItemFormDTO itemFormDTO = ItemFormDTO.of(item);

            // 현재 상품의 대표이미지 검색
            QItemImg qItemImg = QItemImg.itemImg;
            ItemImg itemRepImg = queryFactory.select(qItemImg)
                    .from(qItemImg)
                    .where(qItemImg.item.id.eq(1L),
                            qItemImg.repimgYn.eq("Y"))
                    .fetchOne();
            ItemImgDTO itemRepImgDTO = ItemImgDTO.entityToDto(itemRepImg);

            itemFormDTO.setItemRepImgDTO(itemRepImgDTO);
            itemFormDTOList.add(itemFormDTO);
        }

        itemFormDTOList.forEach(itemFormDTO -> System.out.println("=================" + itemFormDTO));
    }

    public MemberDetailDTO getShopMember(Long memberId, Long itemId){

        /* 회원 프로필사진 저장 */
        ProfileImage profileImage = profileImageRepository.findByMemberId(memberId);
        ProfileImageDTO profileImageDTO = ProfileImageDTO.entityToDTO(profileImage);

        /* 회원 상품 리스트 저장 */
        QItem qItem = QItem.item;
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        //현재 상품 번호만 제외하고 등록 순으로 판매자 다른 상품 조회
        List<Item> itemList = queryFactory.select(qItem)
                .from(qItem)
                .where(qItem.member.id.eq(memberId))
                .where(qItem.id.ne(itemId))
                .orderBy(qItem.id.desc())
                .limit(4)
                .fetch();
        List<ItemFormDTO> itemFormDTOList = new ArrayList<>();
        for(Item item : itemList){
            /* 이미지, 태그 정보는 들어가지 않음 (쓰이지 않음) */
            ItemFormDTO itemFormDTO = ItemFormDTO.of(item);

            // 현재 상품의 대표이미지 검색
            QItemImg qItemImg = QItemImg.itemImg;
            ItemImg itemRepImg = queryFactory.select(qItemImg)
                    .from(qItemImg)
                    .where(qItemImg.item.id.eq(itemId),
                            qItemImg.repimgYn.eq("Y"))
                    .fetchOne();
            ItemImgDTO itemRepImgDTO = ItemImgDTO.entityToDto(itemRepImg);

            itemFormDTO.setItemRepImgDTO(itemRepImgDTO);
        }
        /* 회원 정보 저장 */
        Member member = memberRepository.findById(memberId).orElseThrow(() ->
                new EntityNotFoundException("해당 회원이 존재하지 않습니다. id = " + memberId));

        MemberDetailDTO memberDetailDTO = MemberDetailDTO.entityToDTOSafe(member);
        /* 프로필 사진 저장 */
        memberDetailDTO.setProfileImageDTO(profileImageDTO);
        /* 판매수 저장 */
        memberDetailDTO.setSellCount(itemSellRepository.countByMemberId(memberId));
        /* 판매자 다른 상품 목록 저장 */
        memberDetailDTO.setItemFormDTOList(itemFormDTOList);

        return memberDetailDTO;
    }

    @Test
    public void currentMemberEmail() {
        // OAuth2 인증 상황 가정
        OAuth2AuthenticationToken authentication = mock(OAuth2AuthenticationToken.class);
        OAuth2User oauthUser = mock(OAuth2User.class);
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("email", "user@example.com");

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(oauthUser);
        when(authentication.getAuthorizedClientRegistrationId()).thenReturn("google");
        when(oauthUser.getAttributes()).thenReturn(attributes);

        String email = memberService.currentMemberEmail();

        assertEquals("user@example.com", email);
    }


    @Test
    void registerMemberTest() throws Exception {
        // 테스트에 사용할 MemberFormDTO 생성
        MemberFormDTO memberFormDTO = new MemberFormDTO();
        memberFormDTO.setName("Test Name");
        memberFormDTO.setEmail("test@example.com");
        memberFormDTO.setPwd("password");
        memberFormDTO.setNickname("TestNickname");
        memberFormDTO.setPhone("01012345678");
        memberFormDTO.setBirth(LocalDate.now());
        memberFormDTO.setGender(Gender.m);
        memberFormDTO.setUserAddr("Test Address");
        memberFormDTO.setUserDaddr("Detailed Address");
        memberFormDTO.setUserStnum("123-45");

        // 회원 등록 메소드 실행
        memberService.registerMember(memberFormDTO, passwordEncoder);

        // 회원 정보가 데이터베이스에 저장되었는지 확인
        Member result = memberRepository.findByEmail(memberFormDTO.getEmail());

        // 검증
        assertNotNull(result);
        assertEquals(memberFormDTO.getEmail(), result.getEmail());
        assertTrue(passwordEncoder.matches("password", result.getPwd()));
        assertEquals(memberFormDTO.getNickname(), result.getNickname());
        assertEquals(memberFormDTO.getPhone(), result.getPhone());
        // 다른 필드에 대한 검증도 추가할 수 있습니다.
    }

}