package com.example.ddd.loan.preReview.infrastucture;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.ddd.loan.preReview.domain.entity.PreReview;
import com.example.ddd.loan.preReview.domain.repository.PreReviewRepository;
import com.example.ddd.loan.preReview.domain.vo.PreReviewStatus;

/**
 * 사전 심사 리포지토리 구현체
 */
@Repository
public class PreReviewRepositoryImpl implements PreReviewRepository {

	private final PreReviewJpaRepository preReviewJpaRepository;

	public PreReviewRepositoryImpl(PreReviewJpaRepository springDataRepository) {
		this.preReviewJpaRepository = springDataRepository;
	}

	@Override
	public PreReview save(PreReview preReview) {
		return preReviewJpaRepository.save(preReview);
	}

	@Override
	public Optional<PreReview> findById(Long id) {
		return preReviewJpaRepository.findById(id);
	}

	@Override
	public Optional<PreReview> findByApplicationId(String applicationId) {
		return Optional.ofNullable(preReviewJpaRepository.findByApplicationId(applicationId));
	}

	@Override
	public List<PreReview> findByCustomerIdAndStatusIn(String customerId, List<PreReviewStatus> statuses) {
		return preReviewJpaRepository.findByCustomerIdAndStatusIn(customerId, statuses);
	}

	@Override
	public void delete(PreReview preReview) {
		preReviewJpaRepository.delete(preReview);
	}
}
