package com.example.ddd.loan.loanapplication.domain.event;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 도메인 이벤트 기본 클래스
 */
@Getter
public abstract class ApplicationDomainEvent {
    private final String eventId;
    private final LocalDateTime occurredAt;

    protected ApplicationDomainEvent() {
        this.eventId = UUID.randomUUID().toString();
        this.occurredAt = LocalDateTime.now();
    }
}

