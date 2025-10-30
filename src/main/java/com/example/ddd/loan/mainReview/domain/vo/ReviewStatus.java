package com.example.ddd.loan.mainReview.domain.vo;

import lombok.Getter;

/**
 * 본 심사 상태를 나타내는 열거형 (Enum)
 */
@Getter
public enum ReviewStatus {

    REVIEW_PENDING("본 심사 대기") {
        @Override
        public boolean canTransitionTo(ReviewStatus newStatus) {
            return newStatus == REVIEWING;
        }
    },

    REVIEWING("본 심사 중") {
        @Override
        public boolean canTransitionTo(ReviewStatus newStatus) {
            return newStatus == DOC_REQUESTED || newStatus == APPROVAL_PENDING;
        }
    },

    DOC_REQUESTED("서류 보완 요청") {
        @Override
        public boolean canTransitionTo(ReviewStatus newStatus) {
            return newStatus == REVIEWING || newStatus == APPROVAL_PENDING;
        }
    },

    APPROVAL_PENDING("승인 대기") {
        @Override
        public boolean canTransitionTo(ReviewStatus newStatus) {
            return false;
        }
    };

    private final String description;

    ReviewStatus(String description) {
        this.description = description;
    }

    /* 상태 전이 가능 여부 체크 */
    public abstract boolean canTransitionTo(ReviewStatus newStatus);

    public boolean canBeAssignedReviewer() {
        return this == REVIEW_PENDING;
    }

    public boolean canAnalyzeFinancials() {
        return this == REVIEWING || this == DOC_REQUESTED;
    }

    public boolean canRequestAdditionalDocuments() {
        return this == REVIEWING || this == DOC_REQUESTED;
    }

    public boolean canSubmitAdditionalDocuments() {
        return this == DOC_REQUESTED;
    }

    public boolean canRecordReviewDecision() {
        return this == REVIEWING || this == DOC_REQUESTED;
    }

    public boolean canCompleteReview() {
        return this == REVIEWING;
    }
}