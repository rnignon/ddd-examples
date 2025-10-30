package com.example.ddd.loan.preReview.domain.vo;

/**
 * 사전 심사 상태
 */
public enum PreReviewStatus {
	PENDING,        // 심사 대기
	IN_PROGRESS,    // 심사 진행 중
	PASSED,         // 통과
	FAILED          // 탈락
}
