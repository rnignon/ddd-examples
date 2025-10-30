package com.example.ddd.loan.loanapplication.infrastructure;

import com.example.ddd.loan.loanapplication.domain.entity.LoanApplication;
import com.example.ddd.loan.loanapplication.domain.vo.ApplicationId;
import com.example.ddd.loan.loanapplication.domain.vo.ApplicationStatus;
import com.example.ddd.loan.loanapplication.domain.vo.CustomerId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanApplicationJpaRepository extends JpaRepository<LoanApplication, ApplicationId> {
    // 고객 ID로 신청서 목록 조회
    List<LoanApplication> findByCustomerId(CustomerId customerId);
     // 고객 ID와 상태로 신청서 존재 여부 확인
    boolean existsByCustomerIdAndStatus(CustomerId customerId, ApplicationStatus status);
}
