package com.example.ddd.loan.approval.domain.vo;

import java.util.List;

public enum ApprovalStatus {
    REQUESTED {
        @Override
        public boolean canTransitionTo(ApprovalStatus newStatus) {
            return newStatus == ASSIGNED;
        }
    },
    ASSIGNED {
        @Override
        public boolean canTransitionTo(ApprovalStatus newStatus) {
            return newStatus == REVIEW;
        }
    },
    REVIEW {
        @Override
        public boolean canTransitionTo(ApprovalStatus newStatus) {
            return newStatus == APPROVED
                || newStatus == CONDITIONAL_APPROVED
                || newStatus == REJECTED;
        }
    },
    APPROVED {
        @Override
        public boolean canTransitionTo(ApprovalStatus newStatus) {
            return false;
        }
    },
    CONDITIONAL_APPROVED {
        @Override
        public boolean canTransitionTo(ApprovalStatus newStatus) {
            return false;
        }
    },
    REJECTED {
        @Override
        public boolean canTransitionTo(ApprovalStatus newStatus) {
            return false;
        }
    }
    ;

    private static final List<ApprovalStatus> COMPLETED = List.of(
        ApprovalStatus.APPROVED, ApprovalStatus.REJECTED);

    public abstract boolean canTransitionTo(ApprovalStatus newStatus);

    public boolean isCompleted() {
        return COMPLETED.contains(this);
    }
}
