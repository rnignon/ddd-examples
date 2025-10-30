package com.example.ddd.loan.approval.domain.service;


import com.example.ddd.loan.approval.domain.entity.Approval;
import com.example.ddd.loan.approval.domain.vo.ApprovalAmount;
import java.math.BigInteger;

public class ApprovalDomainService {

    private final ApprovalPolicy assignmentPolicy;

    public ApprovalDomainService(ApprovalPolicy assignmentPolicy) {
        this.assignmentPolicy = assignmentPolicy;
    }

    /**
     * 승인 건을 누가 처리해야 하는지 결정
     * 금액에 따라 AI / MANAGER 결정
     * @param approval
     * @return
     */
    public AssignmentType decideAssignmentType(Approval approval) {
        return assignmentPolicy.decideAssignmentType(approval);
    }

    public void validateApproval(Approval approval, ApprovalAmount approvalAmount, Long reviewerId) {

        if (approval.getStatus().isCompleted()) {
            throw new IllegalStateException("이미 승인/반려가 완료된 건입니다.");
        }

        if (approvalAmount == null || approvalAmount.amount().compareTo(BigInteger.ZERO) <= 0) {
            throw new IllegalArgumentException("승인 금액은 0보다 커야 합니다.");
        }

        if (reviewerId == null) {
            throw new IllegalStateException("승인자는 필수입니다.");
        }
    }
}
