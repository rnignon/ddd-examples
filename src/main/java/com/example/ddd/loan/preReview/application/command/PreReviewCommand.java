package com.example.ddd.loan.preReview.application.command;

/**
 * 사전 심사 시작 커맨드 (입력 DTO)
 */
public record PreReviewCommand(
	String applicationId,
	String customerId,
	int age,
	long annualIncome,
	double dsr
) {
	public PreReviewCommand {
		if (age < 0) {
			throw new IllegalArgumentException("나이는 0보다 커야 합니다");
		}
		if (annualIncome < 0) {
			throw new IllegalArgumentException("연소득은 0보다 커야 합니다");
		}
	}
}
