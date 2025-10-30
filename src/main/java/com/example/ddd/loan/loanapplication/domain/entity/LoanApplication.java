package com.example.ddd.loan.loanapplication.domain.entity;

import com.example.ddd.loan.loanapplication.domain.event.ApplicationCancelledEvent;
import com.example.ddd.loan.loanapplication.domain.event.ApplicationSubmittedEvent;
import com.example.ddd.loan.loanapplication.domain.vo.*;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 대출신청 Aggregate Root
 *
 * Aggregate 경계:
 * - LoanApplication (루트)
 * - Document (내부 엔티티)
 * - LoanRequest, ApplicantInfo, ConsentInfo (값 객체)
 *
 * 역할:
 * - 고객의 대출 신청 정보를 관리하는 Aggregate의 루트 엔티티
 * - 신청서 작성, 제출, 취소 등의 생명주기를 관리
 * - 신청 상태 전이와 비즈니스 규칙을 보장
 */
@Entity
@Table(name = "loan_application")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoanApplication extends AbstractAggregateRoot<LoanApplication> {

    private static final int MIN_AGE = 19;
    private static final int MAX_AGE = 65;

    @EmbeddedId
    private ApplicationId id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "customer_id", nullable = false))
    })
    private CustomerId customerId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "product_code", nullable = false, length = 10))
    })
    private ProductCode productCode;

    @Embedded
    private LoanRequest loanRequest;

    @Embedded
    private ApplicantInfo applicantInfo;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id")
    private final List<Document> documents = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ApplicationStatus status;

    @Embedded
    private ConsentInfo consentInfo;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private LocalDateTime submittedAt;

    // ========== 생성 메서드 ==========

    /**
     * 대출 신청 생성 팩토리 메서드
     * 모든 비즈니스 규칙을 여기서 검증
     *
     * 비즈니스 규칙:
     * - 신청서는 항상 DRAFT 상태로 시작
     * - 고객과 상품 정보는 필수
     */
    public static LoanApplication create(
            ApplicationId id,
            CustomerId customerId,
            ProductCode productCode) {

        validateRequiredFields(customerId, productCode);

        LoanApplication application = new LoanApplication();
        application.id = id;
        application.customerId = customerId;
        application.productCode = productCode;
        application.status = ApplicationStatus.DRAFT;
        application.createdAt = LocalDateTime.now();
        application.updatedAt = LocalDateTime.now();

        return application;
    }

    private static void validateRequiredFields(CustomerId customerId, ProductCode productCode) {
        if (customerId == null) {
            throw new IllegalArgumentException("고객 ID는 필수입니다");
        }
        if (productCode == null) {
            throw new IllegalArgumentException("상품 코드는 필수입니다");
        }
    }

    // ========== 비즈니스 로직 ==========

    /**
     * 대출 요청 정보 입력
     *
     * 비즈니스 규칙:
     * - DRAFT 상태에서만 수정 가능
     * - 최소 신청 금액 검증 (신용대출 100만원, 담보대출 500만원)
     */
    public void inputLoanRequest(LoanRequest loanRequest) {
        validateModifiable();
        validateLoanRequest(loanRequest);

        this.loanRequest = loanRequest;
        this.updatedAt = LocalDateTime.now();
    }

    private void validateLoanRequest(LoanRequest loanRequest) {
        if (loanRequest == null) {
            throw new IllegalArgumentException("대출 요청 정보는 필수입니다");
        }

        Money minAmount = loanRequest.getLoanType().getMinimumAmount();
        if (loanRequest.getRequestAmount().isLessThan(minAmount)) {
            throw new IllegalArgumentException(
                    String.format("최소 신청 금액은 %s원 입니다", minAmount.getAmount()));
        }
    }

    /**
     * 신청자 정보 입력
     *
     * 비즈니스 규칙:
     * - DRAFT 상태에서만 수정 가능
     * - 연령 제한 검증 (만 19세 이상 65세 이하)
     */
    public void inputApplicantInfo(ApplicantInfo applicantInfo) {
        validateModifiable();
        validateApplicantInfo(applicantInfo);

        this.applicantInfo = applicantInfo;
        this.updatedAt = LocalDateTime.now();
    }

    private void validateApplicantInfo(ApplicantInfo applicantInfo) {
        if (applicantInfo == null) {
            throw new IllegalArgumentException("신청자 정보는 필수입니다");
        }

        int age = applicantInfo.getAge();
        if (age < MIN_AGE || age > MAX_AGE) {
            throw new IllegalArgumentException(
                    String.format("만 %d세 이상 %d세 이하만 신청 가능합니다. 현재 나이: %d",
                            MIN_AGE, MAX_AGE, age));
        }
    }

    /**
     * 서류 업로드
     *
     * 비즈니스 규칙:
     * - DRAFT 상태에서만 가능
     * - 같은 유형의 서류는 최신 것으로 교체
     */
    public void uploadDocument(Document document) {
        validateModifiable();

        if (document == null) {
            throw new IllegalArgumentException("서류 정보는 필수입니다");
        }

        // 같은 유형의 기존 서류가 있으면 교체
        documents.removeIf(doc -> doc.isSameType(document));
        documents.add(document);

        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 동의 처리
     *
     * 비즈니스 규칙:
     * - DRAFT 상태에서만 가능
     * - 개인정보 수집 및 신용정보 조회 동의는 필수
     */
    public void consent(ConsentInfo consentInfo) {
        validateModifiable();
        validateConsent(consentInfo);

        this.consentInfo = consentInfo;
        this.updatedAt = LocalDateTime.now();
    }

    private void validateConsent(ConsentInfo consentInfo) {
        if (consentInfo == null) {
            throw new IllegalArgumentException("동의 정보는 필수입니다");
        }

        if (!consentInfo.isAllRequiredConsented()) {
            throw new IllegalStateException("필수 동의 항목이 모두 동의되지 않았습니다");
        }
    }

    /**
     * 신청서 제출
     *
     * 비즈니스 규칙:
     * - DRAFT 상태에서만 제출 가능
     * - 모든 필수 정보 입력 완료
     * - 필수 서류 모두 업로드
     * - 필수 동의 완료
     */
    public void submit() {
        validateModifiable();
        validateCompleteness();

        this.status = ApplicationStatus.SUBMITTED;
        this.submittedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        // 도메인 이벤트 발행 (사전심사 시작 트리거)
        registerEvent(new ApplicationSubmittedEvent(
                this.id,
                this.customerId,
                this.loanRequest.getRequestAmount(),
                this.submittedAt
        ));
    }

    private void validateCompleteness() {
        if (loanRequest == null) {
            throw new IllegalStateException("대출 요청 정보를 입력해주세요");
        }

        if (applicantInfo == null) {
            throw new IllegalStateException("신청자 정보를 입력해주세요");
        }

        if (consentInfo == null || !consentInfo.isAllRequiredConsented()) {
            throw new IllegalStateException("필수 동의를 완료해주세요");
        }

        if (!hasAllRequiredDocuments()) {
            throw new IllegalStateException("필수 서류를 모두 업로드해주세요");
        }
    }

    private boolean hasAllRequiredDocuments() {
        List<DocumentType> requiredTypes = List.of(
                DocumentType.ID_CARD,
                DocumentType.INCOME_PROOF,
                DocumentType.EMPLOYMENT_CERTIFICATE
        );

        return requiredTypes.stream()
                .allMatch(type -> documents.stream()
                        .anyMatch(doc -> doc.isType(type)));
    }

    /**
     * 신청 취소
     *
     * 비즈니스 규칙:
     * - 심사 전 단계(DRAFT, SUBMITTED)에서만 취소 가능
     * - 본인만 취소 가능
     */
    public void cancel(CustomerId requesterId) {
        // 비즈니스 규칙: 본인만 취소 가능
        if (!this.customerId.equals(requesterId)) {
            throw new IllegalStateException("본인의 신청만 취소할 수 있습니다");
        }

        // 비즈니스 규칙: 취소 가능한 상태인지 확인
        if (!status.canBeCancelled()) {
            throw new IllegalStateException(
                    String.format("%s 상태에서는 취소할 수 없습니다", status.getDescription()));
        }

        this.status = ApplicationStatus.CANCELLED;
        this.updatedAt = LocalDateTime.now();

        // 도메인 이벤트 발행
        registerEvent(new ApplicationCancelledEvent(
                this.id,
                this.customerId,
                this.loanRequest
        ));
    }

    /**
     * 진행 상태 변경
     *
     * - 사전심사, 본심사 Aggregate에서 상태를 업데이트할 때 사용
     */
    public void updateStatus(ApplicationStatus newStatus) {
        if (!this.status.canTransitionTo(newStatus)) {
            throw new IllegalStateException(
                    String.format("잘못된 상태 전이입니다: %s -> %s",
                            this.status.getDescription(), newStatus.getDescription()));
        }

        this.status = newStatus;
        this.updatedAt = LocalDateTime.now();
    }

    // ========== 검증 메서드 ==========

    private void validateModifiable() {
        if (!status.isModifiable()) {
            throw new IllegalStateException(
                    String.format("DRAFT 상태에서만 수정할 수 있습니다. 현재 상태: %s",
                            status.getDescription()));
        }
    }

    // ========== 조회 메서드 ==========

    /**
     * 읽기 전용 서류 목록 반환
     * 외부에서 직접 수정 불가 (캡슐화)
     */
    public List<Document> getDocuments() {
        return Collections.unmodifiableList(documents);
    }

    /**
     * 제출 완료 여부 확인
     */
    public boolean isSubmitted() {
        return status == ApplicationStatus.SUBMITTED;
    }

    /**
     * 취소 가능 여부 확인
     */
    public boolean isCancellable() {
        return status.canBeCancelled();
    }

    /**
     * 서류 개수 조회
     */
    public int getDocumentCount() {
        return documents.size();
    }
}
