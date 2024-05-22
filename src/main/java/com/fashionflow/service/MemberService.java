
package com.fashionflow.service;

import com.fashionflow.dto.*;
import com.fashionflow.entity.*;
import com.fashionflow.repository.ItemSellRepository;
import com.fashionflow.repository.MemberRepository;
import com.fashionflow.repository.ProfileImageRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    private final ProfileImageRepository profileImageRepository;

    private final ItemSellRepository itemSellRepository;

    private final ProfileImgService profileImgService;

    private final EntityManager em;

    private final FileService fileService;

    @Value("${profileImgLocation}")
    private String profileImgLocation;


    public void registerMember(MemberFormDTO memberFormDTO, PasswordEncoder passwordEncoder) throws Exception {
        // Bcrypt 인코드
        String encodedPassword = passwordEncoder.encode(memberFormDTO.getPwd());

        // Member 객체 생성
        Member member = Member.builder()
                .name(memberFormDTO.getName())
                .email(memberFormDTO.getEmail())
                .pwd(encodedPassword)
                .nickname(memberFormDTO.getNickname())
                .phone(memberFormDTO.getPhone())
                .birth(memberFormDTO.getBirth())
                .gender(memberFormDTO.getGender())
                .userAddr(memberFormDTO.getUserAddr())
                .userDaddr(memberFormDTO.getUserDaddr())
                .userStnum(memberFormDTO.getUserStnum())
                .regdate(LocalDateTime.now())
                .build();

        // 중복 확인
        checkDuplicate(member);

        // 회원 저장
        memberRepository.save(member);

        // 프로필 이미지 저장 및 회원에 연결
        if (memberFormDTO.getProfileImageFile() != null) {
            ProfileImage profileImage = new ProfileImage();
            profileImgService.saveProfileImage(profileImage, memberFormDTO.getProfileImageFile());
            profileImage.setMember(member);
            member.setProfileImage(profileImage); // 연관 관계 설정

            // 프로필 이미지 저장
            profileImageRepository.save(profileImage);
        }
    }



    //중복 체크 메소드
    public void checkDuplicate(Member member) {
        Member checkMember = memberRepository.findByEmailOrPhoneOrNickname(member.getEmail(), member.getPhone(), member.getNickname());
        if (checkMember != null) {
            if (checkMember.getEmail().equals(member.getEmail())) {
                throw new IllegalStateException("이미 가입된 이메일입니다.");
            }
            if (checkMember.getPhone().equals(member.getPhone())) {
                throw new IllegalStateException("이미 가입된 휴대폰 번호입니다.");
            }
            if (checkMember.getNickname().equals(member.getNickname())) {
                throw new IllegalStateException("중복되는 닉네임입니다.");
            }
        }
    }


    //사용자 인증처리
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email);

        if(member == null){
            throw new UsernameNotFoundException(email);
        }

        return User.builder()
                .username(member.getEmail())
                .password(member.getPwd())
                .roles(member.getRole().toString())
                .build();
    }

    //현재 로그인된 사용자 security를 이용해 email 반환
    public String currentMemberEmail() {

        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // OAuth 2.0 인증
        if (authentication instanceof OAuth2AuthenticationToken) {

            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            OAuth2User oauthUser = oauthToken.getPrincipal();

            Map<String, Object> attributes = oauthToken.getPrincipal().getAttributes();

            if ("kakao".equals(oauthToken.getAuthorizedClientRegistrationId())) {
                Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
                // 사용자 정보에서 이메일 속성 추출
                return (String) kakaoAccount.get("email");
            } else if ("naver".equals(oauthToken.getAuthorizedClientRegistrationId())) {
                Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("response");
                // 사용자 정보에서 이메일 속성 추출
                return (String) kakaoAccount.get("email"); }

            // 구글 사용자 정보에서 이메일 속성 추출
            return (String) attributes.get("email");
        }

        //사이트 자체 회원 인증
        else if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName(); // 사용자의 이메일 반환
        }
        return null;
    }

    // 반환된 email로 사용자의 정보 조회 메소드
    public Member findMemberByCurrentEmail() {
        String email = currentMemberEmail();
        if (email != null) {
            return memberRepository.findByEmail(email);
        }
        return null;
    }

    public boolean findUnregisteredOAuthMember() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            return false;
        } else if (findMemberByCurrentEmail() != null){
            return false;
        }

        return true;
    }

    // 회원 정보 업데이트 메서드
    public void updateMember(MemberFormDTO memberFormDTO, PasswordEncoder passwordEncoder) throws Exception {

        // 현재 멤버 정보 가져오기
        Member currentMember = findMemberByCurrentEmail();

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(memberFormDTO.getPwd());

        // 닉네임 중복 검사
        Member existingMember = memberRepository.findByNickname(memberFormDTO.getNickname());
        if (existingMember != null && !existingMember.getId().equals(currentMember.getId())) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        // 프로필 이미지 업데이트
        MultipartFile profileImageFile = memberFormDTO.getProfileImageFile();
        if (profileImageFile != null && !profileImageFile.isEmpty()) {
            // 기존 이미지가 있는지 확인
            ProfileImage existingProfileImage = currentMember.getProfileImage();
            if (existingProfileImage != null) {
                // 기존 이미지 업데이트
                profileImgService.updateProfileImage(existingProfileImage, profileImageFile);
            } else {
                // 새로운 이미지 저장
                ProfileImage newProfileImage = new ProfileImage();
                profileImgService.saveProfileImage(newProfileImage, profileImageFile);
                newProfileImage.setMember(currentMember);
                currentMember.setProfileImage(newProfileImage); // 연관 관계 설정
                profileImageRepository.save(newProfileImage);
            }
        }


        // 회원 정보 업데이트
        currentMember.setPwd(encodedPassword);
        currentMember.setNickname(memberFormDTO.getNickname());
        currentMember.setGender(memberFormDTO.getGender());
        currentMember.setUserStnum(memberFormDTO.getUserStnum());
        currentMember.setUserAddr(memberFormDTO.getUserAddr());
        currentMember.setUserDaddr(memberFormDTO.getUserDaddr());

        memberRepository.save(currentMember);
    }



    // 회원 삭제 메소드
    public void deleteMember(String email) {
        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            throw new IllegalArgumentException("해당 이메일로 등록된 회원을 찾을 수 없습니다.");
        }
        memberRepository.delete(member);

    }

    // 아이디 찾기 메소드
    public String findId(String name, String phone){
        Member member = memberRepository.findByNameAndPhone(name, phone);

        if (member == null){
            throw new IllegalArgumentException("해당 이메일로 등록된 회원을 찾을 수 없습니다.");
        }

        return member.getEmail();
    }

    //판매자 정보 찾기 메소드(상세페이지)
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
                    .where(qItemImg.item.id.eq(item.getId())
                            .and(qItemImg.repimgYn.eq("Y")))
                    .fetchOne();

            System.out.println("===========================대표사진 : "+itemRepImg);
            if(itemRepImg != null){
                ItemImgDTO itemRepImgDTO = ItemImgDTO.entityToDto(itemRepImg);

                itemFormDTO.setItemRepImgDTO(itemRepImgDTO);
            }

            System.out.println(itemFormDTO);


            itemFormDTOList.add(itemFormDTO);
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

    public MemberFormDTO getMemberFormDTOByEmail(String email){

        Member member = memberRepository.findByEmail(email);
        if(member == null){
            throw new EntityNotFoundException("존재하지 않는 사용자입니다. : " + email);
        }

        MemberFormDTO memberFormDTO = MemberFormDTO.entityToDTOSafe(member);

        /* 프로필 사진 */
        ProfileImage profileImage = profileImageRepository.findByMemberId(member.getId());
        /* 프로필 사진이 있으면 DTO에 추가 */
        if(profileImage != null){
            memberFormDTO.setProfileImageDTO(ProfileImageDTO.entityToDTO(profileImage));
        }

        return memberFormDTO;
    }

}
