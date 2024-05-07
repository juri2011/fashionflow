package com.fashionflow.repository;

import com.fashionflow.entity.ReportItem;
import com.fashionflow.entity.ReportMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportMemberRepository extends JpaRepository<ReportMember, Long> {
    public List<ReportMember> findAllByOrderByRegdateDesc();

    public Page<ReportMember> findAllByOrderByIdDesc(Pageable pageable);
}
