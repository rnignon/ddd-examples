package com.example.ddd.loan.mainReview.domain.repository;

import com.example.ddd.loan.mainReview.domain.entity.MainReview;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MainReviewRepository {
    MainReview save(MainReview mainReview);
    Optional<MainReview> findById(Long mainReviewId);
}
