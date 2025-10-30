package com.example.ddd.loan.loanapplication.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 고객 ID 값 객체
 */
@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomerId {

    private String value;

    private CustomerId(String value) {
        validateValue(value);
        this.value = value;
    }

    public static CustomerId of(String value) {
        return new CustomerId(value);
    }

    private void validateValue(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("고객 ID는 필수입니다");
        }
    }
}