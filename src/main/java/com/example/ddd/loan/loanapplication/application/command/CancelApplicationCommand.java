package com.example.ddd.loan.loanapplication.application.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CancelApplicationCommand {
    private String applicationId;
    private String requesterId;  // 본인 확인용
}
