package com.fashionflow.service;

import com.fashionflow.constant.Gender;
import com.fashionflow.dto.ItemFormDTO;
import com.fashionflow.dto.MemberFormDTO;
import com.fashionflow.dto.chat.ChatMessageDTO;
import com.fashionflow.dto.chat.ChatRoomDTO;
import com.fashionflow.entity.ChatMessage;
import com.fashionflow.entity.ChatRoom;
import com.fashionflow.entity.Item;
import com.fashionflow.entity.Member;
import com.fashionflow.repository.ChatMessageRepository;
import com.fashionflow.repository.ChatRoomRepository;
import com.fashionflow.repository.ItemRepository;
import com.fashionflow.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ChatServiceTest {

    @Autowired
    ChatRoomRepository chatRoomRepository;

    @Autowired
    ChatMessageRepository chatMessageRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ItemService itemService;

    @Autowired
    MemberService memberService;

    /**
     * 특정 채팅방의 UUID를 통해 해당 채팅방의 채팅 내역을 가져오는 메서드.
     *
     * @param uuid 채팅방의 UUID
     * @return 해당 채팅방의 채팅 내역을 담은 List<ChatMessageDTO>
     * @throws EntityNotFoundException 채팅방이 존재하지 않을 경우 예외 발생
     */
    public List<ChatMessageDTO> getChatHistory(String uuid) {
        // UUID를 통해 채팅방을 조회하고, 존재하지 않으면 예외를 던짐
        ChatRoom chatRoom = chatRoomRepository.findByUuid(uuid).orElseThrow(() ->
                new EntityNotFoundException("채팅방이 존재하지 않습니다. uuid = " + uuid));

        // 채팅방 ID로 해당 채팅방의 모든 메시지를 시간 순서대로 가져옴
        List<ChatMessage> chatMessageList = chatMessageRepository.findByChatRoomIdOrderByIdAsc(chatRoom.getId());

        // 메시지 엔티티 리스트를 DTO 리스트로 변환
        List<ChatMessageDTO> chatHistory = new ArrayList<>();
        for (ChatMessage chatMessage : chatMessageList) {
            ChatMessageDTO chatMessageDTO = ChatMessageDTO.entityToDTO(chatMessage);
            chatHistory.add(chatMessageDTO);
        }

        return chatHistory;
    }

    /**
     * 특정 채팅방의 채팅 내역을 출력하는 테스트 메서드.
     * 테스트 목적으로 하드코딩된 UUID를 사용하여 채팅 내역을 조회하고 출력함.
     */
    @Test
    @Commit
    public void printChatHistory() {
        // 테스트용 하드코딩된 UUID
        String uuid = "44a48077-5719-4563-88b8-c80828e54f00";
        // 채팅 내역 조회
        List<ChatMessageDTO> chatHistory = getChatHistory(uuid);
        // 채팅 내역 출력
        chatHistory.forEach(chat -> System.out.println("================" + chat));
    }

    /**
     * 테스트를 위한 다수의 멤버를 생성하는 헬퍼 메서드.
     *
     * @param length 생성할 멤버의 수
     */
    private void createMember(int length) {
        for (int i = 1; i <= length; i++) {
            // 멤버 폼 DTO 생성
            MemberFormDTO memberFormDTO = MemberFormDTO.builder()
                    .name("test" + i)
                    .email("test" + i + "@test.com")
                    .pwd("123456")
                    .confirmPwd("123456")
                    .nickname("테스트" + i)
                    .phone("010-1111-111" + i)
                    .birth(LocalDate.of(1990 + i, i, i))
                    .gender(Gender.f)
                    .userAddr("테스트 주소" + i)
                    .userDaddr("테스트 상세주소" + i)
                    .userStnum("12345")
                    .build();

            // 멤버 엔티티 생성 및 비밀번호 인코딩
            Member member = Member.builder()
                    .name(memberFormDTO.getName())
                    .email(memberFormDTO.getEmail())
                    .pwd(passwordEncoder.encode(memberFormDTO.getPwd()))
                    .nickname(memberFormDTO.getNickname())
                    .phone(memberFormDTO.getPhone())
                    .birth(memberFormDTO.getBirth())
                    .gender(memberFormDTO.getGender())
                    .userAddr(memberFormDTO.getUserAddr())
                    .userDaddr(memberFormDTO.getUserDaddr())
                    .userStnum(memberFormDTO.getUserStnum())
                    .regdate(LocalDateTime.now())
                    .build();

            // 멤버 저장
            memberRepository.save(member);
        }
    }

    /**
     * 특정 상품 ID를 통해 판매자의 이메일을 찾는 메서드.
     *
     * @param itemId 조회할 상품의 ID
     * @return 해당 상품의 판매자 이메일
     * @throws EntityNotFoundException 상품이 존재하지 않을 경우 예외 발생
     */
    private String findSellerEmail(Long itemId) {
        // 상품 ID로 상품 조회, 존재하지 않으면 예외 발생
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("상품이 존재하지 않습니다 id : " + itemId));
        // 상품의 판매자 멤버 객체 반환
        Member seller = item.getMember();

        return seller.getEmail();
    }

    /**
     * 새로운 채팅방을 생성하고 저장하는 테스트 메서드.
     * 테스트를 위해 회원을 두 명 생성하고, 해당 회원들 간의 채팅방을 생성.
     */
    @Test
    @Commit
    public void createRoom() {
        Long itemId = 2L; // 생성할 채팅방의 상품 ID
        createMember(2); // 두 명의 테스트 회원 생성

        // 상품 및 회원 정보 조회
        ItemFormDTO item = ItemFormDTO.of(itemRepository.findItemById(itemId));
        MemberFormDTO seller = MemberFormDTO.entityToDTO(memberRepository.findByEmail("test1@test.com"));
        MemberFormDTO buyer = MemberFormDTO.entityToDTO(memberRepository.findByEmail("test2@test.com"));

        // 채팅방 DTO 생성
        ChatRoomDTO chatRoomDTO = ChatRoomDTO.create(item.getItemName(), item, buyer, seller);
        // 채팅방 엔티티 생성
        ChatRoom chatRoom = ChatRoom.createChatRoom(chatRoomDTO);
        // 채팅방 저장
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        // 생성된 채팅방 정보 출력 및 검증
        System.out.println(savedChatRoom);
        assertEquals(chatRoom, savedChatRoom);
    }

    /**
     * 특정 조건에 따라 채팅방의 중복 여부를 확인하는 테스트 메서드.
     * 이미 존재하는 채팅방이 있는지 확인하고, 결과를 출력.
     */
    @Test
    public void checkDuplicateRoom() {
        Long itemId = 2L; // 확인할 상품 ID
        String sellerEmail = "test1@test.com"; // 판매자 이메일
        String buyerEmail = "test2@test.com"; // 구매자 이메일

        // 상품 ID, 판매자 이메일, 구매자 이메일을 통해 채팅방 조회
        Optional<ChatRoom> chatRoom =
                chatRoomRepository.findByItemIdAndBuyerEmailAndSellerEmail(itemId, buyerEmail, sellerEmail);

        // 채팅방 존재 여부 출력
        if (chatRoom.isPresent()) {
            System.out.println("채팅방 존재함 : " + chatRoom);
        } else {
            System.out.println("채팅방 없음 : " + chatRoom);
        }
    }
}
