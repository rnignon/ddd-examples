package com.example.ddd.loan.loanapplication.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * 금액 값 객체
 */
@Embeddable
@Getter
@EqualsAndHashCode
public class Money {

    private BigDecimal amount;

    protected Money() {
        this.amount = BigDecimal.ZERO;
    }

    private Money(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("금액은 null일 수 없습니다");
        }
        this.amount = amount;
    }

    public static Money of(BigDecimal amount) {
        return new Money(amount);
    }

    public boolean isPositive() {
        return amount.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isLessThan(Money other) {
        return amount.compareTo(other.amount) < 0;
    }

    public boolean isGreaterThan(Money other) {
        return amount.compareTo(other.amount) > 0;
    }
}