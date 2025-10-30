package com.example.ddd.loan.approval.application;

import com.example.ddd.loan.approval.domain.ApprovalRepository;
import com.example.ddd.loan.approval.domain.entity.Approval;
import com.example.ddd.loan.approval.domain.service.ApprovalDomainService;
import com.example.ddd.loan.approval.domain.service.AssignmentType;
import com.example.ddd.loan.approval.domain.vo.ApprovalAmount;
import com.example.ddd.loan.approval.domain.vo.ApplicationId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApprovalService {

    private final ApprovalRepository approvalRepository;

    private final ApprovalDomainService approvalDomainService;

    /**
     * 승인 생성
     */
    @Transactional
    public Long createApproval(Long applicationId, ApprovalAmount amount) {
        approvalRepository.findByApplicationId(applicationId)
            .ifPresent(a -> {
                throw new IllegalStateException("해당 신청에 대한 승인 건이 이미 존재합니다.");
            });

        Approval approval = Approval.create(ApplicationId.of(applicationId), amount);
        Approval saved = approvalRepository.save(approval);
        return saved.getId();
    }

    /**
     * 심사관 배정
     */
    @Transactional
    public void assign(Long approvalId) {
        Approval approval = approvalRepository.findById(approvalId)
            .orElseThrow(() -> new IllegalArgumentException("승인 대상을 찾을 수 없습니다."));

        AssignmentType assignmentType = approvalDomainService.decideAssignmentType(approval);

        // assignmentType 에 따라 심사관 배정
        Long reviewerId = 0L;

        approval.assignTo(reviewerId);
    }

    /**
     * 승인 확정
     */
    @Transactional
    public void approve(Long approvalId, ApprovalAmount approvedAmount, Long reviewerId) {

        Approval approval = approvalRepository.findById(approvalId)
            .orElseThrow(() -> new IllegalArgumentException("승인 대상을 찾을 수 없습니다."));

        // 본 심사 통과 여부 확인 필요
        // throw new IllegalStateException("사전심사를 통과하지 않은 건은 승인할 수 없습니다.");

        approvalDomainService.validateApproval(approval, approvedAmount, reviewerId);

        approval.approve(reviewerId);
    }

    /**
     * 4. 반려
     */
    @Transactional
    public void reject(Long approvalId, String reasons) {
        Approval approval = approvalRepository.findById(approvalId)
            .orElseThrow(() -> new IllegalArgumentException("승인 대상을 찾을 수 없습니다."));

        approval.reject(reasons);
    }
}
