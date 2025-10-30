package com.example.ddd.loan.loanapplication.infrastructure;

import com.example.ddd.loan.loanapplication.domain.entity.LoanApplication;
import com.example.ddd.loan.loanapplication.domain.repository.LoanApplicationRepository;
import com.example.ddd.loan.loanapplication.domain.vo.ApplicationId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LoanApplicationRepositoryImpl implements LoanApplicationRepository {

    private final LoanApplicationJpaRepository jpaRepository;

    @Override
    public LoanApplication save(LoanApplication application) {
        return jpaRepository.save(application);
    }

    @Override
    public Optional<LoanApplication> findById(ApplicationId id) {
        return jpaRepository.findById(id);
    }
}