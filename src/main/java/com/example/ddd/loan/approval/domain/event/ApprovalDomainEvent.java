package com.example.ddd.loan.approval.domain.event;

import java.time.LocalDateTime;

public abstract class ApprovalDomainEvent {
    private final LocalDateTime occurredAt;

    protected ApprovalDomainEvent() {
        this.occurredAt = LocalDateTime.now();
    }
}
