package com.fashionflow.service;

import com.fashionflow.repository.ReportMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportMemberService {

    public final ReportMemberRepository reportMemberRepository;


}
