package com.example.ddd.loan.loanapplication.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 상품 코드 값 객체
 */
@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductCode {

    private String value;

    private ProductCode(String value) {
        validateFormat(value);
        this.value = value;
    }

    public static ProductCode of(String value) {
        return new ProductCode(value);
    }

    private void validateFormat(String value) {
        if (value == null || value.length() != 10) {
            throw new IllegalArgumentException("상품 코드는 10자리여야 합니다");
        }
    }
}