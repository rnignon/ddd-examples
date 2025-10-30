package com.example.ddd.loan.loanapplication.domain.event;

import com.example.ddd.loan.loanapplication.domain.vo.ApplicationId;
import com.example.ddd.loan.loanapplication.domain.vo.CustomerId;
import com.example.ddd.loan.loanapplication.domain.vo.LoanRequest;
import lombok.Getter;

/**
 * 대출 신청 취소 이벤트
 *
 * - 진행 중인 심사 프로세스 중단
 * - 취소 알림 발송
 * - 통계 데이터 업데이트
 */
@Getter
public class ApplicationCancelledEvent extends ApplicationDomainEvent {
    private final ApplicationId applicationId;
    private final CustomerId customerId;
    private final LoanRequest loanRequest;

    public ApplicationCancelledEvent(
            ApplicationId applicationId,
            CustomerId customerId,
            LoanRequest loanRequest) {
        super();
        this.applicationId = applicationId;
        this.customerId = customerId;
        this.loanRequest = loanRequest;
    }
}