package com.example.ddd.loan.preReview.application.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.example.ddd.loan.preReview.domain.entity.PreReview;
import com.example.ddd.loan.preReview.domain.vo.PreReviewStatus;

/**
 * 사전 심사 결과 (출력 DTO)
 */
public record PreReviewResult(
	Long id,
	String applicationId,
	String customerId,
	boolean isPassed,
	PreReviewStatus status,
	String resultMessage,
	List<String> violations,
	LocalDateTime completedAt
) {
	public static PreReviewResult from(PreReview preReview) {
		return new PreReviewResult(
			preReview.getId(),
			preReview.getApplicationId(),
			preReview.getCustomerId(),
			preReview.isPassed(),
			preReview.getStatus(),
			preReview.getResultMessage(),
			preReview.getViolations(),
			preReview.getCompletedAt()
		);
	}
}
