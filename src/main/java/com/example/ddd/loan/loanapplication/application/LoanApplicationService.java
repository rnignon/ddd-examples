package com.example.ddd.loan.loanapplication.application;

import com.example.ddd.loan.loanapplication.application.command.CancelApplicationCommand;
import com.example.ddd.loan.loanapplication.application.command.CreateApplicationCommand;
import com.example.ddd.loan.loanapplication.application.command.SubmitApplicationCommand;
import com.example.ddd.loan.loanapplication.application.dto.ApplicationResponse;
import com.example.ddd.loan.loanapplication.domain.entity.LoanApplication;
import com.example.ddd.loan.loanapplication.domain.repository.LoanApplicationRepository;
import com.example.ddd.loan.loanapplication.domain.service.ApplicationIdGenerator;
import com.example.ddd.loan.loanapplication.domain.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoanApplicationService {

    private final LoanApplicationRepository applicationRepository;
    private final ApplicationIdGenerator idGenerator;

    /**
     * 대출 신청서 작성
     *
     * - 고객이 대출 상품을 선택
     * - 신청서 생성 (DRAFT 상태)
     */
    @Transactional
    public ApplicationResponse createApplication(CreateApplicationCommand command) {
        log.info("대출 신청서 작성 시작: customerId={}, productCode={}",
                command.getCustomerId(), command.getProductCode());

        ApplicationId applicationId = idGenerator.generate();

        CustomerId customerId = CustomerId.of(command.getCustomerId());
        ProductCode productCode = ProductCode.of(command.getProductCode());

        LoanApplication application = LoanApplication.create(
                applicationId,
                customerId,
                productCode
        );

        applicationRepository.save(application);

        log.info("대출 신청서 작성 완료: applicationId={}", applicationId.getValue());

        return ApplicationResponse.from(application);
    }

    /**
     * 대출 신청서 제출
     *
     * - 고객이 대출 정보를 입력
     * - 신청자 정보 입력
     * - 동의 항목 체크
     * - 신청서 제출
     */
    @Transactional
    public ApplicationResponse submitApplication(SubmitApplicationCommand command) {
        log.info("대출 신청서 제출 시작: applicationId={}", command.getApplicationId());

        // 1. 신청서 조회
        ApplicationId applicationId = ApplicationId.of(command.getApplicationId());
        LoanApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("신청서를 찾을 수 없습니다"));

        // 2. 대출 정보 입력
        LoanRequest loanRequest = LoanRequest.of(command);
        application.inputLoanRequest(loanRequest);

        // 3. 신청자 정보 입력
        ApplicantInfo applicantInfo = ApplicantInfo.of(command);
        application.inputApplicantInfo(applicantInfo);

        // 4. 동의 정보 입력
        ConsentInfo consentInfo = ConsentInfo.of(command);
        application.consent(consentInfo);

        // 5. 제출 (이벤트 발행 포함)
        application.submit();

        // 6. 저장
        applicationRepository.save(application);

        log.info("대출 신청서 제출 완료: applicationId={}, status={}",
                applicationId.getValue(), application.getStatus());

        return ApplicationResponse.from(application);
    }

    /**
     * 대출 신청 취소
     *
     * - 고객이 신청을 취소한다
     * - 본인 확인
     * - 취소 가능 여부 확인
     * - 취소 처리
     */
    @Transactional
    public ApplicationResponse cancelApplication(CancelApplicationCommand command) {
        log.info("대출 신청 취소 시작: applicationId={}, requesterId={}",
                command.getApplicationId(), command.getRequesterId());

        // 1. 신청서 조회
        ApplicationId applicationId = ApplicationId.of(command.getApplicationId());
        LoanApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("신청서를 찾을 수 없습니다"));

        // 2. VO 생성
        CustomerId requesterId = CustomerId.of(command.getRequesterId());

        // 3. 취소 처리 (이벤트도 발행 포함)
        application.cancel(requesterId);

        // 4. 저장
        applicationRepository.save(application);

        log.info("대출 신청 취소 완료: applicationId={}, status={}",
                applicationId.getValue(), application.getStatus());

        return ApplicationResponse.from(application);
    }

    /**
     * 신청서 조회
     */
    @Transactional(readOnly = true)
    public ApplicationResponse getApplication(String applicationId) {
        log.info("대출 신청서 조회: applicationId={}", applicationId);

        ApplicationId id = ApplicationId.of(applicationId);
        LoanApplication application = applicationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("신청서를 찾을 수 없습니다"));

        return ApplicationResponse.from(application);
    }
}
