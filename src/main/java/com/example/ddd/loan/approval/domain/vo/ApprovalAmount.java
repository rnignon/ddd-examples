package com.example.ddd.loan.approval.domain.vo;

import java.math.BigInteger;

public record ApprovalAmount(BigInteger amount) {

    // canonical constructor (record가 만들어주는 그 생성자에 우리가 검증 추가)
    public ApprovalAmount {
        if (amount == null) {
            throw new IllegalArgumentException("금액은 null일 수 없습니다");
        }
        if (amount.signum() < 0) {
            throw new IllegalArgumentException("금액은 음수일 수 없습니다");
        }
    }

    public ApprovalAmount(int value) {
        this(BigInteger.valueOf(value));
    }
}
