package com.example.ddd.loan.loanapplication.application.command;

import com.example.ddd.loan.loanapplication.domain.vo.LoanType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class SubmitApplicationCommand {
    private String applicationId;

    // 대출 정보
    private LoanType loanType;
    private BigDecimal requestAmount;
    private int loanPeriodMonths;
    private String loanPurpose;

    // 신청자 정보
    private String name;
    private String birthDate;
    private String phoneNumber;
    private String address;
    private BigDecimal annualIncome;
    private String employmentType;
    private int employmentPeriodMonths;

    // 동의 정보
    private boolean personalInfoConsent;
    private boolean creditInfoConsent;
    private boolean marketingConsent;
}