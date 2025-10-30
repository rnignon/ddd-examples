package com.example.ddd.loan.loanapplication.domain.vo;

import com.example.ddd.loan.loanapplication.application.command.SubmitApplicationCommand;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 신청자 정보 값 객체
 *
 * - 개인정보, 소득정보는 함께 다뤄지기 때문
 */
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicantInfo {

    @Column(name = "applicant_name", length = 50)
    private String name;

    @Column(name = "birth_date", length = 8)
    private String birthDate;  // YYYYMMDD

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "address", length = 200)
    private String address;

    @Column(name = "annual_income", precision = 15, scale = 0)
    private BigDecimal annualIncome;

    @Column(name = "employment_type", length = 20)
    private String employmentType;

    @Column(name = "employment_period_months")
    private int employmentPeriodMonths;

    private static void validateApplicantInfo(String name, String birthDate, BigDecimal annualIncome) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("신청자 이름은 필수입니다");
        }
        if (birthDate == null || !birthDate.matches("\\d{8}")) {
            throw new IllegalArgumentException("생년월일은 YYYYMMDD 형식이어야 합니다");
        }
        if (annualIncome == null || annualIncome.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("연소득은 0 이상이어야 합니다");
        }
    }

    /**
     * 나이 계산 (만 나이)
     * 도메인 로직을 값 객체에 캡슐화
     */
    public int getAge() {
        int birthYear = Integer.parseInt(birthDate.substring(0, 4));
        int currentYear = java.time.LocalDate.now().getYear();
        return currentYear - birthYear;
    }

    /**
     * 연소득 조회 (Money 타입으로 변환)
     */
    public Money getAnnualIncomeAsMoney() {
        return Money.of(annualIncome);
    }

    public static ApplicantInfo of(SubmitApplicationCommand command) {

        String name = command.getName();
        String birthDate = command.getBirthDate();
        String phoneNumber = command.getPhoneNumber();
        String address = command.getAddress();
        BigDecimal annualIncome = command.getAnnualIncome();
        String employmentType = command.getEmploymentType();
        int employmentPeriodMonths = command.getEmploymentPeriodMonths();

        validateApplicantInfo(name, birthDate, annualIncome);
        return new ApplicantInfo(
                name,
                birthDate,
                phoneNumber,
                address,
                annualIncome,
                employmentType,
                employmentPeriodMonths
        );
    }
}

