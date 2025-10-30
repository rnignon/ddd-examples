package com.example.ddd.loan.mainReview.infrastructure.repository;

import com.example.ddd.loan.mainReview.domain.entity.MainReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMainReviewRepository extends JpaRepository<MainReview, Long> {
}
