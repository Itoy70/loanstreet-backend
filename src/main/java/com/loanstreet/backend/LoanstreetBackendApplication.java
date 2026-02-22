package com.loanstreet.backend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class LoanstreetBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoanstreetBackendApplication.class, args);
        log.info("LoanStreet Backend started successfully");
    }
}
