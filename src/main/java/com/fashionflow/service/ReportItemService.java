package com.fashionflow.service;

import com.fashionflow.repository.ReportItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportItemService {
    public final ReportItemRepository reportItemRepository;

}
