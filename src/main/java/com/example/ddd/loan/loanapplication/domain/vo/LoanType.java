package com.example.ddd.loan.loanapplication.domain.vo;

import lombok.Getter;

import java.math.BigDecimal;

/**
 * 대출 유형
 *
 * 비즈니스 규칙:
 * - 각 대출 유형별 최소 금액이 다름
 * - 신용대출: 100만원 이상
 * - 담보대출: 500만원 이상
 */
@Getter
public enum LoanType {
    CREDIT_LOAN("신용대출", Money.of(new BigDecimal("1000000"))),
    MORTGAGE_LOAN("주택담보대출", Money.of(new BigDecimal("5000000"))),
    AUTO_LOAN("자동차담보대출", Money.of(new BigDecimal("5000000")));

    private final String description;
    private final Money minimumAmount;

    LoanType(String description, Money minimumAmount) {
        this.description = description;
        this.minimumAmount = minimumAmount;
    }
}