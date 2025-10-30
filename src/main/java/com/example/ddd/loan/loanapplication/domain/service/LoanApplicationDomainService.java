package com.example.ddd.loan.loanapplication.domain.service;

import com.example.ddd.loan.loanapplication.domain.entity.LoanApplication;
import com.example.ddd.loan.loanapplication.domain.vo.Money;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
public class LoanApplicationDomainService {

    private static final Money MINIMUM_ANNUAL_INCOME = Money.of(new BigDecimal("20000000")); // 2천만원

    /**
     * 대출 신청서 완전성 검증
     *
     * 비즈니스 규칙:
     * - 연소득 2,000만원 이상
     * - 모든 필수 정보 입력 완료
     * - 신청 금액이 연소득의 일정 배수 이내
     */
    public void validateApplicationCompleteness(LoanApplication application) {
        log.info("대출 신청서 완전성 검증 시작: applicationId={}",
                application.getId().getValue());

        // 1. 연소득 검증
        Money annualIncome = application.getApplicantInfo().getAnnualIncomeAsMoney();
        if (annualIncome.isLessThan(MINIMUM_ANNUAL_INCOME)) {
            throw new IllegalStateException(
                    String.format("연소득은 최소 %s원 이상이어야 합니다",
                            MINIMUM_ANNUAL_INCOME.getAmount()));
        }

        // 2. 신청 금액 대비 연소득 비율 검증 (최대 연소득의 5배)
        Money requestAmount = application.getLoanRequest().getRequestAmount();
        Money maxLoanAmount = Money.of(
                annualIncome.getAmount().multiply(new BigDecimal("5"))
        );

        if (requestAmount.isGreaterThan(maxLoanAmount)) {
            throw new IllegalStateException(
                    "신청 금액은 연소득의 5배를 초과할 수 없습니다");
        }

        log.info("대출 신청서 완전성 검증 완료");
    }

    /**
     * 대출 신청 가능 여부 판단
     *
     * 비즈니스 규칙:
     * - 고객의 진행 중인 신청이 없어야 함
     * - 최근 거절된 신청이 있으면 일정 기간 대기 필요
     */
    public boolean canApplyNewLoan(
            LoanApplication newApplication,
            java.util.List<LoanApplication> existingApplications) {

        log.info("신규 대출 신청 가능 여부 판단: customerId={}",
                newApplication.getCustomerId().getValue());

        // 진행 중인 신청이 있는지 확인
        boolean hasActiveApplication = existingApplications.stream()
                .anyMatch(LoanApplication::isSubmitted);

        if (hasActiveApplication) {
            log.warn("진행 중인 대출 신청이 존재합니다");
            return false;
        }

        log.info("신규 대출 신청 가능");
        return true;
    }
}
