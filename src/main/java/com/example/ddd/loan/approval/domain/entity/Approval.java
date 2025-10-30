package com.example.ddd.loan.approval.domain.entity;

import com.example.ddd.loan.approval.domain.event.ApprovalApprovedEvent;
import com.example.ddd.loan.approval.domain.event.ApprovalCreatedEvent;
import com.example.ddd.loan.approval.domain.event.ApprovalRejectedEvent;
import com.example.ddd.loan.approval.domain.vo.ApprovalAmount;
import com.example.ddd.loan.approval.domain.vo.ApprovalDecision;
import com.example.ddd.loan.approval.domain.vo.ApprovalStatus;
import com.example.ddd.loan.approval.domain.vo.ApplicationId;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.AbstractAggregateRoot;


/**
 * 승인 (Aggregate Root)
 */
@Entity
@Table(name = "approval")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Approval extends AbstractAggregateRoot<Approval> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "application_id"))
    private ApplicationId applicationId;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "approval_amount", nullable = false))
    private ApprovalAmount approvalAmount;

    @Embedded
    private ApprovalDecision decision;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ApprovalStatus status;

    @Column(name = "reason")
    private String reason;

    @Column(name = "requested_at", nullable = false)
    private LocalDateTime requestedAt;

    @Column(name = "reviewer_id")
    private Long reviewerId;

    @Column(name = "decided_at")
    private LocalDateTime decidedAt;

    // ========== 생성 메서드 ==========

    /**
     * 승인 생성 팩토리 메서드
     */
    public static Approval create(
        ApplicationId applicationId,
        ApprovalAmount approvalAmount) {

        Approval approval = new Approval();
        approval.applicationId = applicationId;
        approval.approvalAmount = approvalAmount;
        approval.status = ApprovalStatus.REQUESTED;
        approval.requestedAt = LocalDateTime.now();

        approval.registerEvent(new ApprovalCreatedEvent(
            approval.applicationId,
            approval.approvalAmount
        ));

        return approval;
    }

    /**
     * 승인권자 배정
     */
    public void assignTo(Long reviewerId) {
        if (!this.status.canTransitionTo(ApprovalStatus.ASSIGNED)) {
            throw new IllegalStateException("이 상태에서는 배정할 수 없습니다.");
        }
        this.reviewerId = reviewerId;
        this.status = ApprovalStatus.ASSIGNED;
    }

    /**
     * 리뷰
     */
    public void inReview() {
        if (!this.status.canTransitionTo(ApprovalStatus.REVIEW)) {
            throw new IllegalStateException("이 상태에서는 심사로 전환할 수 없습니다.");
        }
        this.status = ApprovalStatus.REVIEW;
    }

    /**
     * 승인
     */
    public void approve(Long reviewerId) {
        if (!this.status.canTransitionTo(ApprovalStatus.APPROVED)) {
            throw new IllegalStateException("이 상태에서는 승인할 수 없습니다.");
        }
        if (!reviewerId.equals(this.reviewerId)) {
            throw new IllegalStateException("배정된 승인권자만 승인 가능");
        }
        this.status = ApprovalStatus.APPROVED;
        this.decidedAt = LocalDateTime.now();

        registerEvent(
            new ApprovalApprovedEvent(this.id, this.applicationId.id(), this.approvalAmount));
    }

    /**
     * 거절
     * @param reason
     */
    public void reject(String reason) {
        if (this.status.isCompleted()) {
            throw new IllegalStateException();
        }

        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("거절 사유는 필수입니다.");
        }

        this.status = ApprovalStatus.REJECTED;
        this.reason = reason;
        this.decidedAt = LocalDateTime.now();

        registerEvent(new ApprovalRejectedEvent(this.id, this.applicationId.id(), this.reason));
    }
}
