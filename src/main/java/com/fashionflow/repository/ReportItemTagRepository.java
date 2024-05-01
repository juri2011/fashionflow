package com.fashionflow.repository;

import com.fashionflow.entity.ReportItem;
import com.fashionflow.entity.ReportItemTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportItemTagRepository extends JpaRepository<ReportItemTag, Long> {

    //public List<ReportItemTag> findAllByReportItem(ReportItem reportItem);
    public List<ReportItemTag> findAllByReportItem_Id(Long reportId);
}
