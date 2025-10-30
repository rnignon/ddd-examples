package com.example.ddd.loan.mainReview.domain.vo;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 심사 결정 정보를 나타내는 값 객체 (Value Object)
 */
@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewDecision {

    private String reviewComment;
    private Money recommendedAmount;
    private Double recommendedRate;

    public ReviewDecision(String reviewComment, Money recommendedAmount, Double recommendedRate) {
        validateReviewComment(reviewComment);
        validateRecommendedRate(recommendedRate);

        this.reviewComment = reviewComment;
        this.recommendedAmount = recommendedAmount;
        this.recommendedRate = recommendedRate;
    }

    private void validateReviewComment(String reviewComment) {
        if (reviewComment == null || reviewComment.trim().isEmpty()) {
            throw new IllegalArgumentException("심사 코멘트는 비어 있을 수 없습니다.");
        }
    }

    private void validateRecommendedRate(Double recommendedRate) {
        if (recommendedRate == null || recommendedRate < 0 || recommendedRate > 100) {
            throw new IllegalArgumentException("추천 금리는 0%에서 100% 사이여야 합니다.");
        }
    }
}
