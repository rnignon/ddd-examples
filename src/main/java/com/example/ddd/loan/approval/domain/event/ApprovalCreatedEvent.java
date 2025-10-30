package com.example.ddd.loan.approval.domain.event;

import com.example.ddd.loan.approval.domain.vo.ApprovalAmount;
import com.example.ddd.loan.approval.domain.vo.ApplicationId;

public class ApprovalCreatedEvent extends ApprovalDomainEvent{

    private final ApplicationId applicationId;
    private final ApprovalAmount approvalAmount;
    public ApprovalCreatedEvent(ApplicationId applicationId, ApprovalAmount approvalAmount) {
        super();
        this.applicationId = applicationId;
        this.approvalAmount = approvalAmount;
    }
}
