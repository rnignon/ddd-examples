package com.example.ddd.loan.loanapplication.domain.vo;

import lombok.Getter;

/**
 * 서류 유형
 */
@Getter
public enum DocumentType {
    ID_CARD("신분증"),
    INCOME_PROOF("소득증빙서류"),
    EMPLOYMENT_CERTIFICATE("재직증명서"),
    BANK_STATEMENT("은행 거래내역"),
    OTHER("기타");

    private final String displayName;

    DocumentType(String displayName) {
        this.displayName = displayName;
    }
}