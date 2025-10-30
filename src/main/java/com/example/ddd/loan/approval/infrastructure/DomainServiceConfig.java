package com.example.ddd.loan.approval.infrastructure;

import com.example.ddd.loan.approval.domain.service.ApprovalDomainService;
import com.example.ddd.loan.approval.domain.service.ApprovalPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainServiceConfig {

    @Bean
    public ApprovalPolicy approvalAssignmentPolicy() {
        return new ApprovalPolicy();
    }
    @Bean
    public ApprovalDomainService approvalDomainService(ApprovalPolicy approvalAssignmentPolicy) {
        return new ApprovalDomainService(approvalAssignmentPolicy);
    }

}
