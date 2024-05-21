package com.fashionflow.service;

import com.fashionflow.constant.Gender;
import com.fashionflow.constant.ItemStatus;
import com.fashionflow.constant.Role;
import com.fashionflow.constant.SellStatus;
import com.fashionflow.dto.CategoryDTO;
import com.fashionflow.dto.ItemFormDTO;
import com.fashionflow.dto.ItemImgDTO;
import com.fashionflow.dto.ItemTagDTO;
import com.fashionflow.entity.Item;
import com.fashionflow.entity.ItemImg;
import com.fashionflow.entity.ItemTag;
import com.fashionflow.entity.Member;
import com.fashionflow.repository.ItemImgRepository;
import com.fashionflow.repository.ItemRepository;
import com.fashionflow.repository.ItemTagRepository;
import com.fashionflow.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
class ItemServiceTest {


    @Mock
    private ItemRepository itemRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ItemImgService itemImgService;

    @Mock
    private ItemTagRepository itemTagRepository;

    @Mock
    private ItemImgRepository itemImgRepository;

    @InjectMocks
    private ItemService itemService;

    /* 상품 상세정보 + 이미지 가져오기 */
    /*
    private ItemFormDTO getItemDetail(Long itemId){

        //상품 이미지 번호 순으로 상품이미지 리스트 가져오기


        //Repository에서 파라미터(상품 번호)로 Item 엔티티 가져오기
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new EntityNotFoundException("해당 상품이 존재하지 않습니다. id = " + itemId));

        //Item 객체를 ItemFormDTO로 형변환
        ItemFormDTO itemFormDTO = ItemFormDTO.of(item);

        //위에서 변환시킨 ItemImgDTOList를 itemFormDTO 객체로 가져오기
        itemFormDTO.setItemImgDTOList(itemImgDTOList);

        return itemFormDTO;

    }
*/
  /*  private ItemFormDTO getItemFormDTO(Long itemId,
                                       List<ItemImgDTO> itemImgDTOList,
                                       List<ItemTagDTO> itemTagDTOList){
        //Repository에서 파라미터(상품 번호)로 Item 엔티티 가져오기
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new EntityNotFoundException("해당 상품이 존재하지 않습니다. id = " + itemId));

        //Item 객체를 ItemFormDTO로 형변환
        ItemFormDTO itemFormDTO = ItemFormDTO.of(item);

        //위에서 변환시킨 ItemImgDTOList를 itemFormDTO 객체로 가져오기
        itemFormDTO.setItemImgDTOList(itemImgDTOList);

        itemFormDTO.setItemTagDTOList(itemTagDTOList);

        return itemFormDTO;
    }

    private List<ItemImgDTO> getItemImgDTOList(Long itemId){
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);

        //ItemFormDTO에 저장할 ItemImgDTO 리스트에 위의 데이터 하나씩 저장(데이터 형 변환)
        List<ItemImgDTO> itemImgDTOList = new ArrayList<>();
        for(ItemImg itemImg : itemImgList){
            //ItemImg엔티티 하나씩 가져와서 ItemImgDTO 형으로 변환
            ItemImgDTO itemImgDTO = ItemImgDTO.entityToDto(itemImg);
            itemImgDTOList.add(itemImgDTO);
        }

        return itemImgDTOList;
    }

    private List<ItemTagDTO> getItemTagDTOList(Long itemId){
        *//* 상품 태그 리스트 가져오기 *//*
        List<ItemTag> itemTagList = itemTagRepository.findByItemId(itemId);
        List<ItemTagDTO> itemTagDTOList = new ArrayList<>();
        for(ItemTag itemTag : itemTagList){
            ItemTagDTO itemTagDTO = ItemTagDTO.entityToDTO(itemTag);
            itemTagDTOList.add(itemTagDTO);
        }

        return itemTagDTOList;
    }

    @Test
    public void getItemFormDTOTest(){
        //List<ItemImgDTO> itemImgDTOList = getItemImgDTOList(1L);
        //itemImgDTOList.forEach(itemImgDTO -> System.out.println("======================" + itemImgDTO));
        ItemFormDTO itemFormDTO = getItemFormDTO(1L, getItemImgDTOList(1L), getItemTagDTOList(1L));
        System.out.println("==========================="+itemFormDTO);
    }

    @Test
    public void getItemTagDTOTest(){
        List<ItemTag> itemTagList = itemTagRepository.findByItemId(1L);
        itemTagList.forEach(itemTag -> System.out.println("=================" + itemTag));
    }
*/

    @Test
    public void testSaveItem() throws Exception {
        // Given
        // 테스트용 파일 생성
        MockMultipartFile file1 = new MockMultipartFile("itemImgFile", "test1.jpg", "image/jpeg", "test image content".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("itemImgFile", "test2.jpg", "image/jpeg", "test image content".getBytes());
        List<MultipartFile> fileList = new ArrayList<>();
        fileList.add(file1);
        fileList.add(file2);

        // 테스트용 Member 생성
        Member member = Member.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .pwd("password")
                .nickname("johnny")
                .phone("123-456-7890")
                .birth(LocalDate.of(1990, 1, 1))
                .gender(Gender.m) // 성별 설정
                .mannerScore(5.0) // 매너 점수 설정
                .userStnum("101")
                .userAddr("Test City")
                .userDaddr("Test District")
                .regdate(LocalDateTime.now())
                .intro("Test introduction")
                .role(Role.USER)
                .provider("local")
                .providerId(null)
                .build();

        // 테스트용 ItemFormDTO 생성
        ItemFormDTO itemFormDTO = ItemFormDTO.builder()
                .itemName("Test Item")
                .content("Test item description")
                .price(10000)
                .delivery(2500)
                .address("123 Test Street")
                .itemStatus(ItemStatus.NEW) // 상품 상태 설정
                .sellStatus(SellStatus.SELLING) // 판매 상태 설정
                .memberId(1L) // 판매자의 회원 ID
                .build();



        // memberRepository.findByEmail(userEmail) 메서드가 호출될 때 member를 반환하도록 설정합니다.
        when(memberRepository.findByEmail(anyString())).thenReturn(member);

        // When
        // 상품 저장 메서드 호출
        itemService.saveItem(itemFormDTO, fileList, null, "john@example.com");

        // Then
        // itemRepository.save 메서드가 한 번 호출되었는지 확인합니다.
        verify(itemRepository, times(1)).save(any(Item.class));
        // itemImgService.saveItemImg 메서드가 두 번 호출되었는지 확인합니다.
        verify(itemImgService, times(2)).saveItemImg(any(), any());
        // itemTagRepository.save 메서드가 호출되지 않았는지 확인합니다.
        verify(itemTagRepository, never()).save(any());
    }


}


