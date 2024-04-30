package com.fashionflow.dto;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QReportItemDTO is a Querydsl query type for ReportItemDTO
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReportItemDTO extends EntityPathBase<ReportItemDTO> {

    private static final long serialVersionUID = 1387186004L;

    public static final QReportItemDTO reportItemDTO = new QReportItemDTO("reportItemDTO");

    public QReportItemDTO(String variable) {
        super(ReportItemDTO.class, forVariable(variable));
    }

    public QReportItemDTO(Path<? extends ReportItemDTO> path) {
        super(path.getType(), path.getMetadata());
    }

    public QReportItemDTO(PathMetadata metadata) {
        super(ReportItemDTO.class, metadata);
    }

}

