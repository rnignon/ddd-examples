package com.example.ddd.loan.approval.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ApprovalDecision {

    @Column(name = "approver_id")
    private String approverId;

    @Column(name = "approved_amount")
    private Long approvedAmount;

    @Column(name = "approved_term_months")
    private Integer approvedTermMonths;

    @Column(name = "is_approved")
    private boolean approved;

    protected ApprovalDecision() {
    }

    private ApprovalDecision(String approverId,
        Long approvedAmount,
        Integer approvedTermMonths,
        boolean approved) {
        this.approverId = approverId;
        this.approvedAmount = approvedAmount;
        this.approvedTermMonths = approvedTermMonths;
        this.approved = approved;
    }
}