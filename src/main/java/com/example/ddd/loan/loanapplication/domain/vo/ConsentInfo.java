package com.example.ddd.loan.loanapplication.domain.vo;

import com.example.ddd.loan.loanapplication.application.command.SubmitApplicationCommand;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 동의 정보 값 객체
 */
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ConsentInfo {

    @Column(name = "personal_info_consent", nullable = false)
    private boolean personalInfoConsent;      // 개인정보 수집 동의

    @Column(name = "credit_info_consent", nullable = false)
    private boolean creditInfoConsent;        // 신용정보 조회 동의

    @Column(name = "marketing_consent", nullable = false)
    private boolean marketingConsent;         // 마케팅 동의 (선택)

    @Column(name = "consented_at", nullable = false)
    private LocalDateTime consentedAt;

    /**
     * 정적 팩토리 메서드
     * 동의 시각을 자동으로 설정
     */
    public static ConsentInfo of(SubmitApplicationCommand command) {
        boolean personalInfoConsent = command.isPersonalInfoConsent();
        boolean creditInfoConsent = command.isCreditInfoConsent();
        boolean marketingConsent = command.isMarketingConsent();

        return new ConsentInfo(
                personalInfoConsent,
                creditInfoConsent,
                marketingConsent,
                LocalDateTime.now()
        );
    }

    /**
     * 필수 동의 항목이 모두 동의되었는지 확인
     * 비즈니스 규칙을 값 객체 메서드로 캡슐화
     */
    public boolean isAllRequiredConsented() {
        return personalInfoConsent && creditInfoConsent;
    }
}