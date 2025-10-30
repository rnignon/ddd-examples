package com.example.ddd.loan.preReview.infrastucture;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ddd.loan.preReview.domain.entity.PreReview;
import com.example.ddd.loan.preReview.domain.vo.PreReviewStatus;

interface PreReviewJpaRepository extends JpaRepository<PreReview, Long> {

	PreReview findByApplicationId(String applicationId);

	List<PreReview> findByCustomerIdAndStatusIn(String customerId, List<PreReviewStatus> statuses);
}
