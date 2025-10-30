package com.example.ddd.loan.preReview.domain.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.AbstractAggregateRoot;

import com.example.ddd.loan.preReview.domain.vo.CreditInfo;
import com.example.ddd.loan.preReview.domain.vo.PreReviewStatus;
import com.example.ddd.loan.preReview.domain.event.PreReviewFailedEvent;
import com.example.ddd.loan.preReview.domain.event.PreReviewPassedEvent;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;

/**
 * 사전 심사 (Aggregate Root)
 */
@Entity
@Getter
@Table(name = "pre_review")
public class PreReview extends AbstractAggregateRoot<PreReview> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "application_id", nullable = false)
	private String applicationId;

	@Column(name = "customer_id", nullable = false)
	private String customerId;

	@Embedded
	private CreditInfo creditInfo;

	@Column(name = "is_passed")
	private boolean isPassed;

	@Column(name = "fail_reason", length = 500)
	private String failReason;

	@ElementCollection
	@CollectionTable(name = "pre_review_violations", joinColumns = @JoinColumn(name = "pre_review_id"))
	@Column(name = "violation")
	private List<String> violations = new ArrayList<>();

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private PreReviewStatus status;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@Column(name = "completed_at")
	private LocalDateTime completedAt;

	protected PreReview() {
	}

	private PreReview(String applicationId, String customerId) {
		this.applicationId = applicationId;
		this.customerId = customerId;
		this.status = PreReviewStatus.PENDING;
		this.createdAt = LocalDateTime.now();
		this.violations = new ArrayList<>();
	}

	public static PreReview create(String applicationId, String customerId) {
		return new PreReview(applicationId, customerId);
	}

	public void registerCreditInfo(CreditInfo creditInfo) {
		if (this.status != PreReviewStatus.PENDING) {
			throw new IllegalStateException("신용 정보는 심사 대기 상태에서만 등록할 수 있습니다");
		}
		this.creditInfo = creditInfo;
		this.status = PreReviewStatus.IN_PROGRESS;
	}

	/**
	 * 자동 심사 수행
	 */
	public void conductAutoReview(int age, long annualIncome, double dsr) {
		if (this.status != PreReviewStatus.IN_PROGRESS) {
			throw new IllegalStateException("심사는 진행 중 상태에서만 수행할 수 있습니다");
		}
		if (this.creditInfo == null) {
			throw new IllegalStateException("신용 정보가 등록되어야 심사를 수행할 수 있습니다");
		}

		this.violations.clear();

		// 연령 검증
		if (age < 19 || age > 65) {
			violations.add("연령 기준 미달 (19-65세)");
		}

		// 신용등급 검증 (6등급 이상)
		if (creditInfo.getGrade() > 6) {
			violations.add("신용등급 기준 미달 (6등급 이상 필요)");
		}

		// 소득 검증 (2천만원 이상)
		if (annualIncome < 20_000_000) {
			violations.add("소득 기준 미달 (연 2,000만원 이상 필요)");
		}

		// 연체 이력 검증
		if (creditInfo.hasRecentDelinquency()) {
			violations.add("최근 1년 내 90일 이상 연체 이력 존재");
		}

		// DSR 검증 (40% 이하)
		if (dsr > 40.0) {
			violations.add("DSR 기준 초과 (40% 이하 필요)");
		}

		// 결과 판정
		this.isPassed = violations.isEmpty();
		this.status = isPassed ? PreReviewStatus.PASSED : PreReviewStatus.FAILED;
		this.completedAt = LocalDateTime.now();

		if (!isPassed) {
			this.failReason = String.join(", ", violations);
			// 탈락 이벤트 등록 (AbstractAggregateRoot의 registerEvent 사용)
			registerEvent(PreReviewFailedEvent.from(this.id, this.applicationId, this.customerId, this.violations));
		} else {
			// 통과 이벤트 등록
			registerEvent(PreReviewPassedEvent.from(this.id, this.applicationId, this.customerId));
		}
	}

	public String getResultMessage() {
		if (isPassed) {
			return "사전 심사를 통과하셨습니다. 본심사가 진행됩니다.";
		} else {
			return "사전 심사 결과 다음 기준을 충족하지 못하여 대출이 어렵습니다:\n- "
				+ String.join("\n- ", violations);
		}
	}
}
