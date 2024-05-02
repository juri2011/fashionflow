
package com.fashionflow.service;

import com.fashionflow.dto.MemberFormDTO;
import com.fashionflow.entity.Member;
import com.fashionflow.dto.*;
import com.fashionflow.entity.*;
import com.fashionflow.repository.ItemRepository;
import com.fashionflow.repository.ItemSellRepository;
import com.fashionflow.repository.MemberRepository;
import com.fashionflow.repository.ProfileImageRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    private final ProfileImageRepository profileImageRepository;

    private final ItemSellRepository itemSellRepository;

    private final EntityManager em;

    //회원 등록 메소드
    public void registerMember(MemberFormDTO memberFormDTO, PasswordEncoder passwordEncoder) {



        //Bcrypt 인코드
        String encodedPassword = passwordEncoder.encode(memberFormDTO.getPwd());

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

        //중복 확인
        checkDuplicate(member);

        memberRepository.save(member);

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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
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




    // 회원 정보 업데이트 메서드
    public void updateMember(MemberFormDTO memberFormDTO, PasswordEncoder passwordEncoder) {

        //Bcrypt 인코드
        String encodedPassword = passwordEncoder.encode(memberFormDTO.getPwd());

        // 이메일로 사용자 조회
        Member currentMember = memberRepository.findByEmail(memberFormDTO.getEmail());

        Member exsitingMember = memberRepository.findByNickname(memberFormDTO.getNickname());

        if(exsitingMember != null && !exsitingMember.getId().equals(currentMember.getId())){

            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
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
                    .where(qItemImg.item.id.eq(item.getId()),
                            qItemImg.repimgYn.eq("Y"))
                    .fetchOne();
            ItemImgDTO itemRepImgDTO = ItemImgDTO.entityToDto(itemRepImg);

            itemFormDTO.setItemRepImgDTO(itemRepImgDTO);

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

}
