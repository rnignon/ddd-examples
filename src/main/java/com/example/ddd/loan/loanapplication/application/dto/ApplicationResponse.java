package com.example.ddd.loan.loanapplication.application.dto;

import com.example.ddd.loan.loanapplication.domain.entity.LoanApplication;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class ApplicationResponse {
    private String applicationId;
    private String customerId;
    private String productCode;
    private String status;
    private String statusDescription;
    private BigDecimal requestAmount;

    public static ApplicationResponse from(LoanApplication application) {
        return ApplicationResponse.builder()
                .applicationId(application.getId().getValue())
                .customerId(application.getCustomerId().getValue())
                .productCode(application.getProductCode().getValue())
                .status(application.getStatus().name())
                .statusDescription(application.getStatus().getDescription())
                .requestAmount(application.getLoanRequest() != null ?
                        application.getLoanRequest().getRequestAmount().getAmount() : null)
                .build();
    }
}