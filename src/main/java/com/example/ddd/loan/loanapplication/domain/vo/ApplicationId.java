package com.example.ddd.loan.loanapplication.domain.vo;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 신청번호 값 객체
 *
 * 설계 이유:
 * - 신청번호는 그 자체로 의미를 가지는 값
 */
@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApplicationId {

    @Column(name = "application_id", length = 14)
    private String value;

    private ApplicationId(String value) {
        validateFormat(value);
        this.value = value;
    }

    public static ApplicationId of(String value) {
        return new ApplicationId(value);
    }

    private void validateFormat(String value) {
        if (value == null || !value.matches("\\d{8}\\d{6}")) {
            throw new IllegalArgumentException(
                    "신청번호는 YYYYMMDD + 6자리 순번 형식이어야 합니다");
        }
    }
}