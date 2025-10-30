package com.example.ddd.loan.mainReview.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 재무 분석 정보를 나타내는 값 객체 (Value Object)
 */
@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FinancialAnalysis {

    private boolean incomeVerified;
    private Money totalIncome;
    private Money totalDebt;
    private Money collateralValue;
    private Double repaymentCapacity;

    public FinancialAnalysis(boolean incomeVerified, Money totalIncome, Money totalDebt, Money collateralValue, Double repaymentCapacity) {
        validateRepaymentCapacity(repaymentCapacity);

        this.incomeVerified = incomeVerified;
        this.totalIncome = totalIncome;
        this.totalDebt = totalDebt;
        this.collateralValue = collateralValue;
        this.repaymentCapacity = repaymentCapacity;
    }

    private void validateRepaymentCapacity(Double repaymentCapacity) {
        if (repaymentCapacity == null || repaymentCapacity < 0 || repaymentCapacity > 100) {
            throw new IllegalArgumentException("상환 능력은 0%에서 100% 사이여야 합니다.");
        }
    }
}
