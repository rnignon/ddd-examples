package com.example.ddd.loan.approval.application.event;

import com.example.ddd.loan.approval.domain.event.ApprovalApprovedEvent;
import com.example.ddd.loan.approval.domain.event.ApprovalCreatedEvent;
import com.example.ddd.loan.approval.domain.event.ApprovalRejectedEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class ApprovalEventHandler {

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleApprovalCreated(ApprovalCreatedEvent event) {

    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleApprovalApproved(ApprovalApprovedEvent event) {

    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleApprovalRejected(ApprovalRejectedEvent event) {

    }
}
