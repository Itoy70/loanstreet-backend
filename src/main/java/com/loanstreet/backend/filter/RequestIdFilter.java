package com.loanstreet.backend.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class RequestIdFilter implements Filter {

    public static final String REQUEST_ID_HEADER = "X-Request-ID";
    public static final String REQUEST_ID_MDC_KEY = "requestId";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestId = httpRequest.getHeader(REQUEST_ID_HEADER);
        if (requestId == null || requestId.isBlank()) {
            requestId = generateRequestId();
        } else if (!isValidUuid(requestId)) {
            String generated = generateRequestId();
            log.warn("Invalid X-Request-ID '{}' from client, replacing with {}", requestId, generated);
            requestId = generated;
        }

        MDC.put(REQUEST_ID_MDC_KEY, requestId);
        httpResponse.setHeader(REQUEST_ID_HEADER, requestId);

        try {
            log.debug("Request {} {} - ID: {}",
                    httpRequest.getMethod(), httpRequest.getRequestURI(), requestId);
            chain.doFilter(request, response);
        } finally {
            MDC.remove(REQUEST_ID_MDC_KEY);
        }
    }

    private boolean isValidUuid(String value) {
        try {
            UUID.fromString(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private String generateRequestId() {
        return UUID.randomUUID().toString();
    }
}
