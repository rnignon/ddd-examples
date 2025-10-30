package com.example.ddd.loan.approval.infrastructure;

import com.example.ddd.loan.approval.domain.entity.Approval;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaApprovalRepository extends JpaRepository<Approval, Long> {

    Optional<Approval> findByApplicationId(Long applicationId);
}
