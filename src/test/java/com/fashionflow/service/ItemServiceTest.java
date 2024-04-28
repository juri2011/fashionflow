package com.fashionflow.service;

import com.fashionflow.dto.CategoryDTO;
import com.fashionflow.dto.ItemFormDTO;
import com.fashionflow.dto.ItemImgDTO;
import com.fashionflow.dto.ItemTagDTO;
import com.fashionflow.entity.Item;
import com.fashionflow.entity.ItemImg;
import com.fashionflow.entity.ItemTag;
import com.fashionflow.repository.ItemImgRepository;
import com.fashionflow.repository.ItemRepository;
import com.fashionflow.repository.ItemTagRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ItemServiceTest {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemImgRepository itemImgRepository;

    @Autowired
    ItemTagRepository itemTagRepository;

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
    private ItemFormDTO getItemFormDTO(Long itemId,
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
        /* 상품 태그 리스트 가져오기 */
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


}