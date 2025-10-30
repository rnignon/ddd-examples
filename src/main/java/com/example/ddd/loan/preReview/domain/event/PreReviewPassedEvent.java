package com.example.ddd.loan.preReview.domain.event;

import java.time.LocalDateTime;

/**
 * 사전 심사 통과 이벤트
 *
 * 역할: 사전 심사 통과 시 본심사로 자동 진행하기 위한 도메인 이벤트
 * 도메인 상의 의미: "사전 심사가 통과되었으니, 본심사를 시작해라"
 */
public record PreReviewPassedEvent(
	Long preReviewId,
	String applicationId,
	String customerId,
	LocalDateTime occurredAt
) {
	public static PreReviewPassedEvent from(Long preReviewId, String applicationId, String customerId) {
		return new PreReviewPassedEvent(
			preReviewId,
			applicationId,
			customerId,
			LocalDateTime.now()
		);
	}
}