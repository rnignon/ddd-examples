package com.example.ddd.loan.preReview.domain.vo;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

/**
 * 신용 정보 (Value Object)

 * 역할: 외부 신용평가기관에서 조회한 고객의 신용 정보
 */
@Embeddable
@Getter
public class CreditInfo {

	@Column(name = "credit_grade")
	private int grade;                          // 신용등급 (1-10)

	@Column(name = "credit_score")
	private int score;                          // 신용점수

	@Enumerated(EnumType.STRING)
	@Column(name = "credit_provider", length = 20)
	private CreditProvider provider;            // 신용평가기관

	@Column(name = "has_delinquency")
	private boolean hasDelinquency;             // 연체 이력 유무

	@Column(name = "recent_delinquency_date")
	private LocalDate recentDelinquencyDate;    // 최근 연체일

	@Column(name = "delinquency_days")
	private int delinquencyDays;                // 연체 일수

	@Column(name = "queried_at")
	private LocalDate queriedAt;                // 조회일시

	// JPA 기본 생성자
	protected CreditInfo() {
	}

	public CreditInfo(int grade, int score, CreditProvider provider,
		boolean hasDelinquency, LocalDate recentDelinquencyDate,
		int delinquencyDays, LocalDate queriedAt) {
		this.grade = grade;
		this.score = score;
		this.provider = provider;
		this.hasDelinquency = hasDelinquency;
		this.recentDelinquencyDate = recentDelinquencyDate;
		this.delinquencyDays = delinquencyDays;
		this.queriedAt = queriedAt;
	}

	/**
	 * 최근 1년 내 90일 이상 연체 이력 확인
	 * 비즈니스 규칙: BR-REV-002
	 */
	public boolean hasRecentDelinquency() {
		if (!hasDelinquency) {
			return false;
		}

		LocalDate oneYearAgo = LocalDate.now().minusYears(1);
		return recentDelinquencyDate != null
			&& recentDelinquencyDate.isAfter(oneYearAgo)
			&& delinquencyDays >= 90;
	}
}
