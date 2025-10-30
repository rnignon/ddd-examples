package com.example.ddd.loan.approval.infrastructure;

import com.example.ddd.loan.approval.domain.ApprovalRepository;
import com.example.ddd.loan.approval.domain.entity.Approval;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApprovalRepositoryAdapter implements ApprovalRepository {

    private final JpaApprovalRepository jpaApprovalRepository;

    @Override
    public Approval save(Approval approval) {
        return jpaApprovalRepository.save(approval);
    }

    @Override
    public Optional<Approval> findById(Long approvalId) {
        return jpaApprovalRepository.findById(approvalId);
    }

    @Override
    public Optional<Approval> findByApplicationId(Long applicationId) {
        return jpaApprovalRepository.findByApplicationId(applicationId);
    }
}
