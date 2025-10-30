package com.example.ddd.loan.preReview.domain.event;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 사전 심사 탈락 이벤트
 *
 * 역할: 사전 심사 탈락 시 고객에게 알림 발송 등을 위한 도메인 이벤트
 */
public record PreReviewFailedEvent(
	Long preReviewId,
	String applicationId,
	String customerId,
	List<String> violations,
	LocalDateTime occurredAt
) {
	public static PreReviewFailedEvent from(Long preReviewId, String applicationId,
		String customerId, List<String> violations) {
		return new PreReviewFailedEvent(
			preReviewId,
			applicationId,
			customerId,
			violations,
			LocalDateTime.now()
		);
	}
}
