package com.example.ddd.loan.approval.domain.event;

public class ApprovalRejectedEvent extends ApprovalDomainEvent {

    private final Long approvalId;
    private final Long loanApplicationId;
    private final String reason;

    public ApprovalRejectedEvent(Long approvalId,
        Long loanApplicationId,
        String reason) {
        super();
        this.approvalId = approvalId;
        this.loanApplicationId = loanApplicationId;
        this.reason = reason;
    }
}
