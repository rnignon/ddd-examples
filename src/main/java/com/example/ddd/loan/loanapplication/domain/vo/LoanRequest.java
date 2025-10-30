package com.example.ddd.loan.loanapplication.domain.vo;

import com.example.ddd.loan.loanapplication.application.command.SubmitApplicationCommand;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 대출 요청 정보 값 객체
 *
 * - 신청 금액, 대출 기간 등은 하나의 개념적 전체
 */
@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LoanRequest {

    @Enumerated(EnumType.STRING)
    @Column(name = "loan_type", length = 20)
    private LoanType loanType;

    @Column(name = "request_amount", precision = 15, scale = 0)
    private Money requestAmount;

    @Column(name = "loan_period_months")
    private int loanPeriodMonths;

    @Column(name = "loan_purpose", length = 200)
    private String loanPurpose;

    /**
     * 정적 팩토리 메서드
     * 검증을 포함한 안전한 객체 생성
     */
    public static LoanRequest of(SubmitApplicationCommand command) {
        LoanType loanType = command.getLoanType();
        Money requestAmount = Money.of(command.getRequestAmount());
        int loanPeriodMonths = command.getLoanPeriodMonths();
        String loanPurpose = command.getLoanPurpose();

        validateLoanRequest(loanType, requestAmount, loanPeriodMonths);
        return new LoanRequest(loanType, requestAmount, loanPeriodMonths, loanPurpose);
    }

    private static void validateLoanRequest(LoanType loanType, Money requestAmount, int loanPeriodMonths) {
        if (loanType == null) {
            throw new IllegalArgumentException("대출 유형은 필수입니다");
        }
        if (requestAmount == null || !requestAmount.isPositive()) {
            throw new IllegalArgumentException("신청 금액은 0보다 커야 합니다");
        }
        if (loanPeriodMonths <= 0) {
            throw new IllegalArgumentException("대출 기간은 0보다 커야 합니다");
        }
    }
}