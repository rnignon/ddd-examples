package com.example.ddd.loan.preReview.infrastucture.external;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.example.ddd.loan.preReview.application.port.CreditInfoClient;
import com.example.ddd.loan.preReview.domain.vo.CreditInfo;
import com.example.ddd.loan.preReview.domain.vo.CreditProvider;

/**
 * Mock 신용정보 클라이언트
 */
@Component
public class MockCreditInfoClient implements CreditInfoClient {

	@Override
	public CreditInfo fetchCreditInfo(String customerId, CreditProvider provider) {
		// Mock 데이터 반환
		return new CreditInfo(
			3,              // 신용등급 3등급
			750,            // 신용점수 750
			provider,
			false,          // 연체 없음
			null,
			0,
			LocalDate.now()
		);
	}
}