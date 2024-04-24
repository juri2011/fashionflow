package com.fashionflow.repository;

import com.fashionflow.entity.ReportItem;
import com.fashionflow.entity.ReportMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportMemberRepository extends JpaRepository<ReportMember, Long> {

}
