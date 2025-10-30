package com.example.ddd.loan.approval.domain.service;

import com.example.ddd.loan.approval.domain.entity.Approval;
import java.math.BigInteger;

public class ApprovalPolicy {
    private static final BigInteger AI_REVIEW_LIMIT = BigInteger.valueOf(30_000_000L);

    public AssignmentType decideAssignmentType(Approval approval) {
        if (isOverAiReviewLimit(approval.getApprovalAmount().amount())) {
            return AssignmentType.MANAGER;
        }

        return AssignmentType.AI;
    }

    private boolean isOverAiReviewLimit(BigInteger amount) {
        return amount.compareTo(AI_REVIEW_LIMIT) > 0;
    }
}
