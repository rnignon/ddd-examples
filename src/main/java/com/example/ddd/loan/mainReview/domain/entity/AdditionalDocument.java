package com.example.ddd.loan.mainReview.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 추가 서류 엔티티
 *
 * 애그리거트 경계 내의 내부 엔티티로, 본 심사(Main Review) 애그리거트에 속함
 * 각 서류에 대한 식별이 필요하므로 엔티티로 설계
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdditionalDocument {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String documentName;

    private String requestComment;

    private boolean submitted = false;

    private String fileUrl;

    private LocalDateTime requestedAt;

    private LocalDateTime submittedAt;

    public AdditionalDocument(String documentName, String requestComment) {
        validateDocumentName(documentName);
        validateRequestComment(requestComment);

        this.documentName = documentName;
        this.requestComment = requestComment;
        this.requestedAt = LocalDateTime.now();
    }

    /**
     * 서류 제출 수행
     */
    public void submit(String fileUrl) {
        validateFileUrl(fileUrl);

        this.fileUrl = fileUrl;
        this.submitted = true;
        this.submittedAt = LocalDateTime.now();
    }

    /**
     * 서류 이름 검증
     */
    private void validateDocumentName(String documentName) {
        if (documentName == null || documentName.trim().isEmpty()) {
            throw new IllegalArgumentException("서류 이름은 비어 있을 수 없습니다.");
        }
    }

    /**
     * 요청 코멘트 검증
     */
    private void validateRequestComment(String requestComment) {
        if (requestComment == null || requestComment.trim().isEmpty()) {
            throw new IllegalArgumentException("요청 코멘트는 비어 있을 수 없습니다.");
        }
    }

    /**
     * 제출 파일 URL 검증
     */
    private void validateFileUrl(String fileUrl) {
        if (fileUrl == null || fileUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("파일 URL은 비어 있을 수 없습니다.");
        }
    }
}
