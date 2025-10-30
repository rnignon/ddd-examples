package com.example.ddd.loan.loanapplication.domain.vo;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * 신청 상태
 *
 * 간단한 버전: 상태 전이 규칙을 Map으로 관리
 */
@Getter
public enum ApplicationStatus {

    DRAFT("임시저장"),
    SUBMITTED("신청접수"),
    CANCELLED("신청취소");

    private final String description;

    ApplicationStatus(String description) {
        this.description = description;
    }

    /**
     * 이 상태에서 전이 가능한 상태들
     */
    public List<ApplicationStatus> getAllowedTransitions() {
        return switch (this) {
            case DRAFT -> Arrays.asList(SUBMITTED, CANCELLED);
            case SUBMITTED -> Arrays.asList(CANCELLED);
            case CANCELLED -> Arrays.asList();  // 빈 리스트
        };
    }

    /**
     * 특정 상태로 전이 가능한지 체크
     */
    public boolean canTransitionTo(ApplicationStatus newStatus) {
        return getAllowedTransitions().contains(newStatus);
    }

    /**
     * 취소 가능 여부
     */
    public boolean canBeCancelled() {
        return this == DRAFT || this == SUBMITTED;
    }

    /**
     * 수정 가능 여부
     */
    public boolean isModifiable() {
        return this == DRAFT;
    }
}