package com.example.ddd.loan.preReview.domain.service;

import java.util.List;

import com.example.ddd.loan.preReview.application.command.PreReviewCommand;
import com.example.ddd.loan.preReview.application.dto.PreReviewResult;

public interface PreReviewService {

	/**
	 * Use Case: 사전 심사 시작
	 */
	PreReviewResult startPreReview(PreReviewCommand command);

	/**
	 * Use Case: 사전 심사 결과 조회
	 */
	PreReviewResult getPreReview(String applicationId);

	/**
	 * Use Case: 고객의 진행 중인 사전 심사 목록 조회
	 */
	List<PreReviewResult> getInProgressPreReviews(String customerId);
}
