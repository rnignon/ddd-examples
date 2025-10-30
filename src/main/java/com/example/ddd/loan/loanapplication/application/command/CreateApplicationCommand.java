package com.example.ddd.loan.loanapplication.application.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateApplicationCommand {
    private String customerId;
    private String productCode;
}
