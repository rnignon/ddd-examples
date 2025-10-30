package com.example.ddd.loan.preReview.application.port;

import com.example.ddd.loan.preReview.domain.vo.CreditInfo;
import com.example.ddd.loan.preReview.domain.vo.CreditProvider;

/**
 * 신용정보 조회 포트
 */
public interface CreditInfoClient {
	CreditInfo fetchCreditInfo(String customerId, CreditProvider provider);
}
