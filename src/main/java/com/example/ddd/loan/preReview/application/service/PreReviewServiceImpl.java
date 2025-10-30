package com.example.ddd.loan.preReview.application.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ddd.loan.preReview.application.command.PreReviewCommand;
import com.example.ddd.loan.preReview.application.dto.PreReviewResult;
import com.example.ddd.loan.preReview.application.port.CreditInfoClient;
import com.example.ddd.loan.preReview.domain.vo.CreditInfo;
import com.example.ddd.loan.preReview.domain.vo.CreditProvider;
import com.example.ddd.loan.preReview.domain.entity.PreReview;
import com.example.ddd.loan.preReview.domain.repository.PreReviewRepository;
import com.example.ddd.loan.preReview.domain.service.PreReviewService;
import com.example.ddd.loan.preReview.domain.vo.PreReviewStatus;

/**
 * 사전 심사 Application Service 구현체
 */
@Service
public class PreReviewServiceImpl implements PreReviewService {

	private final PreReviewRepository preReviewRepository;
	private final CreditInfoClient creditInfoClient;

	public PreReviewServiceImpl(PreReviewRepository preReviewRepository,
		CreditInfoClient creditInfoClient) {
		this.preReviewRepository = preReviewRepository;
		this.creditInfoClient = creditInfoClient;
	}

	@Override
	@Transactional
	public PreReviewResult startPreReview(PreReviewCommand command) {
		// 1. 중복 신청 체크
		preReviewRepository.findByApplicationId(command.applicationId())
			.ifPresent(existing -> {
				throw new IllegalStateException("이미 사전 심사가 진행 중입니다: " + command.applicationId());
			});

		// 2. 사전 심사 생성
		PreReview preReview = PreReview.create(
			command.applicationId(),
			command.customerId()
		);

		try {
			// 3. 외부 신용정보 조회
			CreditInfo creditInfo = creditInfoClient.fetchCreditInfo(
				command.customerId(),
				CreditProvider.NICE
			);

			// 4. 신용정보 등록
			preReview.registerCreditInfo(creditInfo);

			// 5. 자동 심사 수행 (여기서 이벤트가 등록됨)
			preReview.conductAutoReview(
				command.age(),
				command.annualIncome(),
				command.dsr()
			);

		} catch (Exception e) {
			throw new RuntimeException("사전 심사 처리 중 오류가 발생했습니다", e);
		}

		// 6. 저장 (Spring Data가 자동으로 이벤트 발행)
		preReviewRepository.save(preReview);

		return PreReviewResult.from(preReview);
	}

	@Override
	public PreReviewResult getPreReview(String applicationId) {
		PreReview preReview = preReviewRepository.findByApplicationId(applicationId)
			.orElseThrow(() -> new IllegalArgumentException(
				"사전 심사를 찾을 수 없습니다: " + applicationId
			));

		return PreReviewResult.from(preReview);
	}

	@Override
	@Transactional(readOnly = true)
	public List<PreReviewResult> getInProgressPreReviews(String customerId) {
		List<PreReviewStatus> inProgressStatuses = Arrays.asList(
			PreReviewStatus.PENDING,
			PreReviewStatus.IN_PROGRESS
		);

		return preReviewRepository
			.findByCustomerIdAndStatusIn(customerId, inProgressStatuses)
			.stream()
			.map(PreReviewResult::from)
			.collect(Collectors.toList());
	}
}