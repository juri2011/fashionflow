package com.fashionflow.repository;

import com.fashionflow.entity.ItemBuy;
import com.fashionflow.entity.ReportItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportItemRepository extends JpaRepository<ReportItem, Long> {

}
