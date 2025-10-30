package com.example.ddd.loan.approval.domain;

import com.example.ddd.loan.approval.domain.entity.Approval;
import java.util.Optional;

public interface ApprovalRepository {

    Approval save(Approval approval);

    Optional<Approval> findById(Long approvalId);

    Optional<Approval> findByApplicationId(Long applicationId);
}
