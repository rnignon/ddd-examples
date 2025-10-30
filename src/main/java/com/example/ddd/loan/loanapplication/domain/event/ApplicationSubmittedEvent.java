package com.example.ddd.loan.loanapplication.domain.event;

import com.example.ddd.loan.loanapplication.domain.vo.ApplicationId;
import com.example.ddd.loan.loanapplication.domain.vo.CustomerId;
import com.example.ddd.loan.loanapplication.domain.vo.Money;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 대출 신청서 제출 이벤트
 *
 * 용도:
 * - 사전심사 프로세스 시작 트리거
 * - 신청 알림 발송
 * - 통계 데이터 업데이트
 */
@Getter
public class ApplicationSubmittedEvent extends ApplicationDomainEvent {
    private final ApplicationId applicationId;
    private final CustomerId customerId;
    private final Money requestAmount;
    private final LocalDateTime submittedAt;

    public ApplicationSubmittedEvent(
            ApplicationId applicationId,
            CustomerId customerId,
            Money requestAmount,
            LocalDateTime submittedAt) {
        super();
        this.applicationId = applicationId;
        this.customerId = customerId;
        this.requestAmount = requestAmount;
        this.submittedAt = submittedAt;
    }
}