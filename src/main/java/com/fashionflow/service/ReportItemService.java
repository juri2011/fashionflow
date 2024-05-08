package com.fashionflow.service;

import com.fashionflow.constant.ReportManageCommand;
import com.fashionflow.constant.ReportStatus;
import com.fashionflow.constant.ReportTagItem;
import com.fashionflow.constant.SellStatus;
import com.fashionflow.dto.ReportItemDTO;
import com.fashionflow.dto.ReportItemTagDTO;
import com.fashionflow.entity.Item;
import com.fashionflow.entity.Member;
import com.fashionflow.entity.ReportItem;
import com.fashionflow.entity.ReportItemTag;
import com.fashionflow.repository.ItemRepository;
import com.fashionflow.repository.MemberRepository;
import com.fashionflow.repository.ReportItemRepository;
import com.fashionflow.repository.ReportItemTagRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportItemService {

    private final ReportItemRepository reportItemRepository;
    private final ReportItemTagRepository reportItemTagRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;

    private final ItemService itemService;

    public Long addReportItem(ReportItemDTO reportItemDTO){
        System.out.println("========================service 진입" + reportItemDTO);
        //신고한 회원 정보
        Member reporterMemeber = memberRepository.findByEmail(reportItemDTO.getReporterMemberEmail());
        //신고대상 상품 정보
        Item reportedItem = itemRepository.findItemById(reportItemDTO.getReportedItemId());
        //신고 항목
        ReportItem reportItem =reportItemDTO.createReportItem();
        reportItem.setReportedItem(reportedItem);
        reportItem.setReporterMember(reporterMemeber);

        ReportItem savedReportItem = reportItemRepository.save(reportItem);
        System.out.println(savedReportItem);

        for(String reportTagItemString : reportItemDTO.getReportTagItemList()){
            //유효한 태그만 추가
            if(EnumUtils.isValidEnum(ReportTagItem.class,reportTagItemString)){
                ReportItemTag reportItemTag = ReportItemTag.builder()
                        .reportTagItem(ReportTagItem.valueOf(reportTagItemString)) //String to Enum
                        .build();
                reportItemTag.setReportItem(reportItem);
                System.out.println("==============================" + reportItemTagRepository.save(reportItemTag));
            }
        }

        return savedReportItem.getId();
    }

    public Page<ReportItemDTO> getReportItemDTOPage(Pageable pageable){


        System.out.println("=====================service==================");
        Page<ReportItem> reportItems = reportItemRepository.findAllByOrderByIdDesc(pageable);
        //Page<ReportItem> reportItems = reportItemRepository.getReportItemPage(pageable);


        System.out.println(reportItems.getTotalPages());

        /* 상품 이미지, 태그 정보는 들어있지 않음 */
        Page<ReportItemDTO> reportItemDTOPage = reportItems.map(m -> {

            //신고 항목 entity를 DTO로 변환
            ReportItemDTO reportItemDTO = ReportItemDTO.entityToDTO(m);

            /* 신고 태그 목록 필드에 들어갈 리스트 생성 */
            //DB에서 현재 신고목록의 태그 목록 받아오기
            List<ReportItemTag> reportItemTagList = reportItemTagRepository.findAllByReportItem_Id(m.getId());

            //신고 항목 DTO에 저장할 리스트 생성
            List<ReportItemTagDTO> reportItemTagDTOList = new ArrayList<>();

            //신고 태그 목록 Entity -> DTO로 변경
            for(ReportItemTag reportItemTag : reportItemTagList){
                ReportItemTagDTO reportItemTagDTO = ReportItemTagDTO.entityToDTO(reportItemTag);
                reportItemTagDTOList.add(reportItemTagDTO);
            }

            //신고 항목의 태그목록 필드 저장
            reportItemDTO.setReportItemTagDTOList(reportItemTagDTOList);

            return reportItemDTO;
        });

        System.out.println(reportItemDTOPage.getTotalPages());


        return reportItemDTOPage;
    }

    public void deleteReportItem(Long id) {
        reportItemRepository.deleteById(id);
    }

    public ReportItemDTO getReportItemDTOById(Long id) {
        ReportItem reportItem = reportItemRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("해당 리뷰가 존재하지 않습니다. id = " + id));

        ReportItemDTO reportItemDTO = ReportItemDTO.entityToDTO(reportItem);
        Long itemId = reportItemDTO.getReportedItemId();

        reportItemDTO.getReportedItem().setItemImgDTOList(itemService.getItemImgDTOList(itemId));
        reportItemDTO.getReportedItem().saveRepimg();

        List<ReportItemTagDTO> reportItemTagDTOList = new ArrayList<>();
        for(ReportItemTag reportItemTag : reportItemTagRepository.findAllByReportItem_Id(id)){

            ReportItemTagDTO reportItemTagDTO = ReportItemTagDTO.entityToDTO(reportItemTag);
            reportItemTagDTOList.add(reportItemTagDTO);
            reportItemDTO.getReportTagItemList().add(reportItemTag.getReportTagItem().getDescription());
        }

        reportItemDTO.setReportItemTagDTOList(reportItemTagDTOList);

        //System.out.println(reportItemDTO);

        return reportItemDTO;
    }

    public Long updateReportItem(ReportItemDTO reportItemDTO) {
        ReportItem targetReportItem = reportItemRepository.findById(reportItemDTO.getId()).orElseThrow(() ->
                new EntityNotFoundException("해당 리뷰가 존재하지 않습니다. id = " + reportItemDTO.getId()));

        //DB에서 태그 불러오기
        List<ReportItemTag> reportItemTagList = reportItemTagRepository.findAllByReportItem_Id(reportItemDTO.getId());
        //태그 이름만 List로 변환
        List<ReportTagItem> reportTagItems = reportItemTagList.stream()
                .map(ReportItemTag::getReportTagItem)
                .toList();

        //Entity를 순회해서 DTO에 없는 태그는 삭제
        for(ReportItemTag reportItemTag : reportItemTagList){
            ReportTagItem reportTagItem = reportItemTag.getReportTagItem();
            if(!reportItemDTO.getReportTagItemList().contains(reportTagItem.name())){
                reportItemTagRepository.deleteById(reportItemTag.getId());
            }
        }
        //DTO가 entity에 없는 태그를 갖고 있다면 추가
        for(String reportTagItemString : reportItemDTO.getReportTagItemList()){

            //유효한 태그만 추가
            if(EnumUtils.isValidEnum(ReportTagItem.class,reportTagItemString)){

                if(reportTagItems.contains(ReportTagItem.valueOf(reportTagItemString))) continue;

                ReportItemTag reportItemTag = ReportItemTag.builder()
                        .reportTagItem(ReportTagItem.valueOf(reportTagItemString)) //String to Enum
                        .build();
                reportItemTag.setReportItem(targetReportItem);
                reportItemTagRepository.save(reportItemTag);
            }
        }

        targetReportItem.setContent(reportItemDTO.getContent());
        //System.out.println("===================== targetReportItem : " + targetReportItem);
        //System.out.println("===================== reportItemDTO : " + reportItemDTO);

        return targetReportItem.getId();
    }

    public void processReportItem(ReportItemDTO reportItemDTO, String command){
        Long itemId = reportItemDTO.getReportedItemId();
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new EntityNotFoundException("해당 상품이 존재하지 않습니다. id = " + itemId));

        if(EnumUtils.isValidEnum(ReportManageCommand.class,command)){
            if(ReportManageCommand.valueOf(command).equals(ReportManageCommand.SUSPEND)){
                //판매중지로 상태 변경
                item.setSellStatus(SellStatus.SUSPENDED);
            }else if(ReportManageCommand.valueOf(command).equals(ReportManageCommand.DELETE)){
                //상품 삭제
                itemRepository.delete(item);
            }
        }else{
            throw new IllegalStateException("유효하지 않은 명령입니다.");
        }

        //신고 처리완료
        Long reportId = reportItemDTO.getId();
        ReportItem reportItem = reportItemRepository.findById(reportId).orElseThrow(() ->
                new EntityNotFoundException("해당 리뷰가 존재하지 않습니다. id = " + reportId));
        reportItem.setReportStatus(ReportStatus.COMPLETE);
    }
}
