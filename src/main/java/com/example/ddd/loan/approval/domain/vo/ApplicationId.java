package com.example.ddd.loan.approval.domain.vo;

import jakarta.persistence.Embeddable;

@Embeddable
public record ApplicationId(Long id) {

    protected ApplicationId() {
        this(null);
    }

    public ApplicationId {
        if (id == null) {
            throw new IllegalArgumentException("null일 수 없습니다");
        }
    }

    public static ApplicationId of(Long applicationId) {
        return new ApplicationId(applicationId);
    }

}
