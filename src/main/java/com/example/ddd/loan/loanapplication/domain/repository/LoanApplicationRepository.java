package com.example.ddd.loan.loanapplication.domain.repository;

import com.example.ddd.loan.loanapplication.domain.entity.LoanApplication;
import com.example.ddd.loan.loanapplication.domain.vo.ApplicationId;

import java.util.Optional;

public interface LoanApplicationRepository {
    LoanApplication save(LoanApplication application);
    Optional<LoanApplication> findById(ApplicationId id);
}
