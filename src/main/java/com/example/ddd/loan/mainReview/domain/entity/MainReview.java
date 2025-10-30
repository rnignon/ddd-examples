package com.example.ddd.loan.mainReview.domain.entity;

import com.example.ddd.loan.mainReview.domain.vo.FinancialAnalysis;
import com.example.ddd.loan.mainReview.domain.vo.ReviewDecision;
import com.example.ddd.loan.mainReview.domain.vo.ReviewStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.util.List;

/**
 * 본 심사 애그리거트 루트 (Main Review Aggregate Root)
 *
 * 애그리거트 경계 :
 * - MainReview (루트)
 * - AdditionalDocument (내부 엔티티)
 * - ReviewStatus, FinancialAnalysis, ReviewDecision (값 객체)
 */
@Entity
@Table(name = "main_reviews")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MainReview extends AbstractAggregateRoot<MainReview> {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long applicationId;

    private Long reviewerId;

    @Enumerated(EnumType.STRING)
    private ReviewStatus reviewStatus;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "main_review_id")
    private List<AdditionalDocument> additionalDocuments;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "incomeVerified", column = @Column(name = "income_verified")),
        @AttributeOverride(name = "totalIncome.amount", column = @Column(name = "total_income")),
        @AttributeOverride(name = "totalDebt.amount", column = @Column(name = "total_debt")),
        @AttributeOverride(name = "collateralValue.amount", column = @Column(name = "collateral_value")),
        @AttributeOverride(name = "repaymentCapacity", column = @Column(name = "repayment_capacity"))
    })
    private FinancialAnalysis financialAnalysis;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "reviewComment", column = @Column(name = "review_comment")),
        @AttributeOverride(name = "recommendedAmount.amount", column = @Column(name = "recommended_amount")),
        @AttributeOverride(name = "recommendedRate", column = @Column(name = "recommended_rate"))
    })
    private ReviewDecision reviewDecision;

    /**
     * 본 심사 생성
     */
    public MainReview(Long applicationId) {
        this.applicationId = applicationId;
        this.reviewStatus = ReviewStatus.REVIEW_PENDING;
    }

    /* 비즈니스 로직 */

    /**
     * 담당 심사역 배정
     */
    public void assignReviewer(Long reviewerId) {
        if (!this.reviewStatus.canBeAssignedReviewer()) {
            throw new IllegalStateException("현재 심사 상태에서는 심사역을 배정할 수 없습니다.");
        }

        if (!canTransitionTo(ReviewStatus.REVIEWING)) {
            throw new IllegalArgumentException("심사 상태를 REVIEWING으로 전환할 수 없습니다.");
        }

        this.reviewerId = reviewerId;
        this.reviewStatus = ReviewStatus.REVIEWING;
    }

    /**
     * 재무 분석 결과 기록
     */
    public void analyzeFinancials(FinancialAnalysis financialAnalysis) {
        if (!this.reviewStatus.canAnalyzeFinancials()) {
            throw new IllegalStateException("현재 심사 상태에서는 재무 분석을 수행할 수 없습니다.");
        }

        this.financialAnalysis = financialAnalysis;
    }

    /**
     * 추가 서류 요청
     */
    public void requestAdditionalDocument(AdditionalDocument additionalDocument) {
        if (!this.reviewStatus.canRequestAdditionalDocuments()) {
            throw new IllegalStateException("현재 심사 상태에서는 추가 서류를 요청할 수 없습니다.");
        }

        if (!canTransitionTo(ReviewStatus.DOC_REQUESTED)) {
            throw new IllegalArgumentException("심사 상태를 DOC_REQUESTED로 전환할 수 없습니다.");
        }

        this.additionalDocuments.add(additionalDocument);
        this.reviewStatus = ReviewStatus.DOC_REQUESTED;
    }

    /**
     * 추가 서류 제출
     */
    public AdditionalDocument submitAdditionalDocument(Long documentId, String fileUrl) {
        if (!this.reviewStatus.canSubmitAdditionalDocuments()) {
            throw new IllegalStateException("현재 심사 상태에서는 추가 서류를 제출할 수 없습니다.");
        }

        AdditionalDocument doc = additionalDocuments.stream()
                .filter(d -> d.getId().equals(documentId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 서류를 찾을 수 없습니다."));

        doc.submit(fileUrl);

        if (additionalDocuments.stream().allMatch(AdditionalDocument::isSubmitted)) {
            if (!canTransitionTo(ReviewStatus.REVIEWING)) {
                throw new IllegalArgumentException("심사 상태를 REVIEWING으로 전환할 수 없습니다.");
            }

            this.reviewStatus = ReviewStatus.REVIEWING;
        }

        return doc;
    }

    /**
     * 심사 결정 결과 기록
     */
    public void recordReviewDecision(ReviewDecision reviewDecision) {
        if (!this.reviewStatus.canRecordReviewDecision()) {
            throw new IllegalStateException("현재 심사 상태에서는 심사 결정 결과를 기록할 수 없습니다.");
        }

        this.reviewDecision = reviewDecision;
    }

    /**
     * 본 심사 완료 처리
     */
    public void completeReview() {
        if (!this.reviewStatus.canCompleteReview()) {
            throw new IllegalStateException("현재 심사 상태에서는 본 심사를 완료할 수 없습니다.");
        }

        if (!canTransitionTo(ReviewStatus.APPROVAL_PENDING)) {
            throw new IllegalArgumentException("심사 상태를 APPROVAL_PENDING으로 전환할 수 없습니다.");
        }

        this.reviewStatus = ReviewStatus.APPROVAL_PENDING;
//        registerEvent(new MainReviewCompletedEvent(this.id, this.applicationId));
    }

    private boolean canTransitionTo(ReviewStatus reviewStatus) {
        return this.reviewStatus.canTransitionTo(reviewStatus);
    }
}
