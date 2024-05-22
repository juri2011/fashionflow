package com.fashionflow.service;

import com.fashionflow.constant.*;
import com.fashionflow.dto.*;
import com.fashionflow.entity.*;
import com.fashionflow.repository.MemberRepository;
import com.fashionflow.repository.ReportMemberRepository;
import com.fashionflow.repository.ReportMemberTagRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportMemberService {

    private final ReportMemberRepository reportMemberRepository;
    private final ReportMemberTagRepository reportMemberTagRepository;
    private final MemberRepository memberRepository;

    private final MemberService memberService;

    // 회원 신고 추가
    public Long addReportMember(ReportMemberDTO reportMemberDTO) {
        System.out.println("========================service 진입" + reportMemberDTO);
        // 신고한 회원 정보
        Member reporterMember = memberRepository.findByEmail(reportMemberDTO.getReporterMemberEmail());
        if (reporterMember == null) {
            throw new EntityNotFoundException("로그인 후 이용해주세요.");
        }
        // 신고대상 회원정보
        Member reportedMember = memberRepository.findByEmail(reportMemberDTO.getReportedMemberEmail());
        // 신고 항목 엔티티
        ReportMember reportMember = reportMemberDTO.createReportMember();
        reportMember.setReportedMember(reportedMember);
        reportMember.setReporterMember(reporterMember);

        // DB에 저장
        ReportMember savedReportMember = reportMemberRepository.save(reportMember);
        System.out.println(savedReportMember);

        // String 배열 -> Enum으로 변환하여 entity에 저장
        for (String reportTagMemberString : reportMemberDTO.getReportMemberTagStringList()) {
            // 유효한 태그만 추가
            if (EnumUtils.isValidEnum(ReportTagMember.class, reportTagMemberString)) {
                ReportMemberTag reportMemberTag = ReportMemberTag.builder()
                        .reportTagMember(ReportTagMember.valueOf(reportTagMemberString)) // String to Enum
                        .build();
                reportMemberTag.setReportMember(reportMember);
                System.out.println("==============================" + reportMemberTagRepository.save(reportMemberTag));
            }
        }

        return savedReportMember.getId();
    }

    // 페이지별 회원 신고 DTO 가져오기
    public Page<ReportMemberDTO> getReportMemberDTOPage(Pageable pageable) {
        System.out.println("=====================service==================");
        Page<ReportMember> reportMembers = reportMemberRepository.findAllByOrderByIdDesc(pageable);

        System.out.println(reportMembers.getTotalPages());

        /* 상품 이미지, 태그 정보는 들어있지 않음 */
        Page<ReportMemberDTO> reportMemberDTOPage = reportMembers.map(m -> {
            // 신고 항목 entity를 DTO로 변환
            ReportMemberDTO reportMemberDTO = ReportMemberDTO.entityToDTO(m);

            /* 신고 태그 목록 필드에 들어갈 리스트 생성 */
            // DB에서 현재 신고목록의 태그 목록 받아오기
            List<ReportMemberTag> reportMemberTagList = reportMemberTagRepository.findAllByReportMember_Id(m.getId());

            // 신고 항목 DTO에 저장할 리스트 생성
            List<ReportMemberTagDTO> reportMemberTagDTOList = new ArrayList<>();

            // 신고 태그 목록 Entity -> DTO로 변경
            for (ReportMemberTag reportMemberTag : reportMemberTagList) {
                ReportMemberTagDTO reportMemberTagDTO = ReportMemberTagDTO.entityToDTO(reportMemberTag);
                reportMemberTagDTOList.add(reportMemberTagDTO);
            }

            // 신고 항목의 태그목록 필드 저장
            reportMemberDTO.setReportMemberTagDTOList(reportMemberTagDTOList);

            return reportMemberDTO;
        });

        System.out.println(reportMemberDTOPage.getTotalPages());

        return reportMemberDTOPage;
    }

    /* 신고항목 아이디로 신고 Entity -> DTO 가져오기 */
    public ReportMemberDTO getReportMemberDTOById(Long id) {
        // 사용자 신고 항목 가져오기
        ReportMember reportMember = reportMemberRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("해당 리뷰가 존재하지 않습니다. id = " + id));

        // 사용자 신고 entity -> DTO 변환
        ReportMemberDTO reportMemberDTO = ReportMemberDTO.entityToDTO(reportMember);
        // String memberEmail = reportMemberDTO.getReportedMemberEmail();

        // 사용자 신고 태그 DTO 리스트
        List<ReportMemberTagDTO> reportMemberTagDTOList = new ArrayList<>();

        // 신고항목 id로 신고항목 태그 리스트 가져오기
        for (ReportMemberTag reportMemberTag : reportMemberTagRepository.findAllByReportMember_Id(id)) {

            // Entity -> DTO로 변환
            ReportMemberTagDTO reportMemberTagDTO = ReportMemberTagDTO.entityToDTO(reportMemberTag);
            reportMemberTagDTOList.add(reportMemberTagDTO);
            // String 리스트에 값 추가
            reportMemberDTO.getReportMemberTagStringList().add(reportMemberTag.getReportTagMember().getDescription());
        }

        reportMemberDTO.setReportMemberTagDTOList(reportMemberTagDTOList);
        System.out.println(reportMemberDTO.getReportMemberTagDTOList());

        return reportMemberDTO;
    }

    // 신고 항목 삭제
    public void deleteReportMember(Long id) {
        reportMemberRepository.deleteById(id);
    }

    // 신고 회원 업데이트
    public Long updateReportMember(ReportMemberDTO reportMemberDTO) {
        ReportMember targetReportMember = reportMemberRepository.findById(reportMemberDTO.getId()).orElseThrow(() ->
                new EntityNotFoundException("해당 리뷰가 존재하지 않습니다. id = " + reportMemberDTO.getId()));

        // DB에서 태그 불러오기
        List<ReportMemberTag> reportMemberTagList =
                reportMemberTagRepository.findAllByReportMember_Id(reportMemberDTO.getId());
        // 태그 이름만 List로 변환
        List<ReportTagMember> reportTagMembers = reportMemberTagList.stream()
                .map(ReportMemberTag::getReportTagMember)
                .toList();

        // Entity를 순회해서 DTO에 없는 태그는 삭제
        for (ReportMemberTag reportMemberTag : reportMemberTagList) {
            ReportTagMember reportTagMember = reportMemberTag.getReportTagMember();
            if (!reportMemberDTO.getReportMemberTagStringList().contains(reportTagMember.name())) {
                reportMemberTagRepository.deleteById(reportMemberTag.getId());
            }
        }
        // DTO가 entity에 없는 태그를 갖고 있다면 추가
        for (String reportTagMemberString : reportMemberDTO.getReportMemberTagStringList()) {

            // 유효한 태그만 추가
            if (EnumUtils.isValidEnum(ReportTagMember.class, reportTagMemberString)) {
                if (reportTagMembers.contains(ReportTagMember.valueOf(reportTagMemberString))) continue;

                ReportMemberTag reportMemberTag = ReportMemberTag.builder()
                        .reportTagMember(ReportTagMember.valueOf(reportTagMemberString)) // String to Enum
                        .build();
                reportMemberTag.setReportMember(targetReportMember);
                reportMemberTagRepository.save(reportMemberTag);
            }
        }

        targetReportMember.setContent(reportMemberDTO.getContent());
        //System.out.println("===================== targetReportMember : " + targetReportMember);
        //System.out.println("===================== reportMemberDTO : " + reportMemberDTO);

        return targetReportMember.getId();
    }

    // 신고 회원 처리
    public void processReportMember(ReportMemberDTO reportMemberDTO, String command) {
        String memberEmail = reportMemberDTO.getReportedMemberEmail();
        Member member = memberRepository.findByEmail(memberEmail);
        if (member == null) {
            throw new EntityNotFoundException("해당 회원이 존재하지 않습니다. email : " + memberEmail);
        }

        if (EnumUtils.isValidEnum(ReportManageCommand.class, command)) {
            if (ReportManageCommand.valueOf(command).equals(ReportManageCommand.SUSPEND)) {
                // 활동정지로 상태 변경
                member.setRole(Role.BLACK);
            }
        } else {
            throw new IllegalStateException("유효하지 않은 명령입니다.");
        }

        // 신고 처리완료
        Long reportId = reportMemberDTO.getId();
        ReportMember reportMember = reportMemberRepository.findById(reportId).orElseThrow(() ->
                new EntityNotFoundException("해당 리뷰가 존재하지 않습니다. id = " + reportId));
        reportMember.setReportStatus(ReportStatus.COMPLETE);
    }
}