package com.example.ddd.loan.loanapplication.infrastructure;

import com.example.ddd.loan.loanapplication.domain.service.ApplicationIdGenerator;
import com.example.ddd.loan.loanapplication.domain.vo.ApplicationId;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ApplicationIdGeneratorImpl implements ApplicationIdGenerator {

    private final AtomicInteger counter = new AtomicInteger(1);

    @Override
    public ApplicationId generate() {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String sequence = String.format("%06d", counter.getAndIncrement());
        return ApplicationId.of(date + sequence);
    }
}
