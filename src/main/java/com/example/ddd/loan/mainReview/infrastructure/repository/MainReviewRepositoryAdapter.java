package com.example.ddd.loan.mainReview.infrastructure.repository;

import com.example.ddd.loan.mainReview.domain.entity.MainReview;
import com.example.ddd.loan.mainReview.domain.repository.MainReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MainReviewRepositoryAdapter implements MainReviewRepository {

    private final JpaMainReviewRepository jpaMainReviewRepository;

    @Override
    public MainReview save(MainReview mainReview) {
        return jpaMainReviewRepository.save(mainReview);
    }

    @Override
    public Optional<MainReview> findById(Long mainReviewId) {
        return jpaMainReviewRepository.findById(mainReviewId);
    }
}
