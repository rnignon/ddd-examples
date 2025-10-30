package com.example.ddd.loan.approval.domain.event;

import com.example.ddd.loan.approval.domain.vo.ApprovalAmount;

public class ApprovalApprovedEvent extends ApprovalDomainEvent {

    private final Long approvalId;
    private final Long loanApplicationId;
    private final ApprovalAmount approvedAmount;

    public ApprovalApprovedEvent(Long approvalId, Long loanApplicationId, ApprovalAmount approvedAmount) {
        super();
        this.approvalId = approvalId;
        this.loanApplicationId = loanApplicationId;
        this.approvedAmount = approvedAmount;
    }
}
