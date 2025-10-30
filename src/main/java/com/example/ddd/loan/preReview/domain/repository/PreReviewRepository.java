package com.example.ddd.loan.preReview.domain.repository;

import java.util.List;
import java.util.Optional;

import com.example.ddd.loan.preReview.domain.vo.PreReviewStatus;
import com.example.ddd.loan.preReview.domain.entity.PreReview;

public interface PreReviewRepository {

	PreReview save(PreReview preReview);

	Optional<PreReview> findById(Long id);

	Optional<PreReview> findByApplicationId(String applicationId);

	List<PreReview> findByCustomerIdAndStatusIn(String customerId, List<PreReviewStatus> statuses);

	void delete(PreReview preReview);
}