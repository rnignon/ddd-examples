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
 * - 서류는 식별자가 있음 (업로드 시각, 파일명)
 * - 같은 유형의 서류도 여러 번 업로드 가능 (교체)
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

    @Column(name = "file_name", nullable = false, length = 255)
    private String fileName;

    @Column(name = "file_path", nullable = false, length = 500)
    private String filePath;

    @Column(name = "file_size", nullable = false)
    private long fileSize;

    @Column(name = "uploaded_at", nullable = false, updatable = false)
    private LocalDateTime uploadedAt;

    /**
     * 생성자를 통한 불변성 보장
     */
    public Document(
            DocumentType type,
            String fileName,
            String filePath,
            long fileSize) {

        validateDocument(type, fileName, filePath, fileSize);

        this.type = type;
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.uploadedAt = LocalDateTime.now();
    }

    private void validateDocument(DocumentType type, String fileName, String filePath, long fileSize) {
        if (type == null) {
            throw new IllegalArgumentException("서류 유형은 필수입니다");
        }
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new IllegalArgumentException("파일명은 필수입니다");
        }
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("파일 경로는 필수입니다");
        }
        if (fileSize <= 0) {
            throw new IllegalArgumentException("파일 크기는 0보다 커야 합니다");
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