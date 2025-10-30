package com.example.ddd.loan.loanapplication.domain.entity;

import com.example.ddd.loan.loanapplication.domain.vo.DocumentType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 서류 엔티티 (Aggregate 내부 엔티티)
 *
 * 설계 이유:
 * - 대출 신청 서류는 대출이 심사에서 통과하지 못하더라도 신청 기록은 남아있기 때문에 신청 제출한 서류도 같이 보관
 * - 같은 유형의 서류도 심사가 통과하지 못할 경우에 여러 번 업로드 가능
 * - Aggregate Root를 통해서만 접근 가능
 */
@Entity
@Table(name = "document")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", nullable = false, length = 30)
    private DocumentType type;

    @Column(name = "uploaded_at", nullable = false, updatable = false)
    private LocalDateTime uploadedAt;

    /**
     * 생성자를 통한 불변성 보장
     */
    public Document(
            DocumentType type) {

        validateDocument(type);

        this.type = type;
        this.uploadedAt = LocalDateTime.now();
    }

    private void validateDocument(DocumentType type) {
        if (type == null) {
            throw new IllegalArgumentException("서류 유형은 필수입니다");
        }
    }

    /**
     * 같은 유형의 서류인지 확인
     */
    public boolean isSameType(Document other) {
        return this.type == other.type;
    }

    /**
     * 특정 유형의 서류인지 확인
     */
    public boolean isType(DocumentType type) {
        return this.type == type;
    }
}