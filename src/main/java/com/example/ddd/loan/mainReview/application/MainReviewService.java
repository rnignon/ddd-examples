package com.example.ddd.loan.mainReview.application;

import com.example.ddd.loan.mainReview.domain.entity.AdditionalDocument;
import com.example.ddd.loan.mainReview.domain.entity.MainReview;
import com.example.ddd.loan.mainReview.domain.repository.MainReviewRepository;
import com.example.ddd.loan.mainReview.domain.vo.FinancialAnalysis;
import com.example.ddd.loan.mainReview.domain.vo.Money;
import com.example.ddd.loan.mainReview.domain.vo.ReviewDecision;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 본 심사 애플리케이션 서비스
 */
@Service
@RequiredArgsConstructor
public class MainReviewService {

    private final MainReviewRepository mainReviewRepository;

    /* 본 심사 생성 */
    @Transactional
    public Long createMainReview(Long applicationId) {
        MainReview mainReview = new MainReview(applicationId);
        mainReviewRepository.save(mainReview);

        return mainReview.getId();
    }

    /* 심사역 배정 */
    @Transactional
    public Long assignReviewer(Long mainReviewId, Long reviewerId) {
        MainReview mainReview = getMainReviewOrThrow(mainReviewId);

        mainReview.assignReviewer(reviewerId);
        mainReviewRepository.save(mainReview);

        return mainReview.getId();
    }

    /* 추가 서류 요청 */
    @Transactional
    public AdditionalDocument requestAdditionalDocument(Long mainReviewId, String documentName, String requestComment) {
        MainReview mainReview = getMainReviewOrThrow(mainReviewId);

        AdditionalDocument additionalDocument = new AdditionalDocument(documentName, requestComment);
        mainReview.requestAdditionalDocument(additionalDocument);
        mainReviewRepository.save(mainReview);

        return additionalDocument;
    }

    /* 추가 서류 제출 */
    @Transactional
    public AdditionalDocument submitAdditionalDocument(Long mainReviewId, Long documentId, String fileUrl) {
        MainReview mainReview = getMainReviewOrThrow(mainReviewId);

        AdditionalDocument doc = mainReview.submitAdditionalDocument(documentId, fileUrl);
        mainReviewRepository.save(mainReview);

        return doc;
    }

    /* 상세 심사 수행 */
    @Transactional
    public Long performDetailedReview(Long mainReviewId) {
        MainReview mainReview = getMainReviewOrThrow(mainReviewId);

        /**
         * 대출 신청자의 소득 서류 진위 확인, 담보 평가, 재무 분석, 상환 능력 평가를 수행
         */

        FinancialAnalysis financialAnalysis = new FinancialAnalysis(
                true,
                Money.of(6000000L),
                Money.of(2000000L),
                Money.of(80000000L),
                40.0
        );

        mainReview.analyzeFinancials(financialAnalysis);
        mainReviewRepository.save(mainReview);

        return mainReview.getId();
    }

    /* 심사 결정 결과 기록 및 심사 완료 처리 */
    @Transactional
    public MainReview finalizeReview(
            Long mainReviewId,
            String reviewComment,
            long recommendedAmount,
            Double recommendedRate
    ) {
        MainReview mainReview = getMainReviewOrThrow(mainReviewId);

        ReviewDecision reviewDecision = new ReviewDecision(reviewComment, Money.of(recommendedAmount), recommendedRate);
        mainReview.recordReviewDecision(reviewDecision);
        mainReview.completeReview();

        return mainReviewRepository.save(mainReview);
    }

    private MainReview getMainReviewOrThrow(Long mainReviewId) {
        return mainReviewRepository.findById(mainReviewId)
                .orElseThrow(() -> new IllegalArgumentException("본 심사 정보를 찾을 수 없습니다. ID: " + mainReviewId));
    }
}
