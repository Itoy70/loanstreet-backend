package com.loanstreet.backend.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.MDC;

import java.io.IOException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class RequestIdFilterTest {

    private RequestIdFilter filter;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain chain;

    @BeforeEach
    void setUp() {
        filter = new RequestIdFilter();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        chain = mock(FilterChain.class);
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/api/loans");
    }

    @AfterEach
    void tearDown() {
        MDC.clear();
    }

    @Test
    void doFilter_withValidRequestId_usesProvidedId() throws IOException, ServletException {
        String providedId = UUID.randomUUID().toString();
        when(request.getHeader(RequestIdFilter.REQUEST_ID_HEADER)).thenReturn(providedId);

        filter.doFilter(request, response, chain);

        verify(response).setHeader(RequestIdFilter.REQUEST_ID_HEADER, providedId);
        verify(chain).doFilter(request, response);
        assertThat(MDC.get(RequestIdFilter.REQUEST_ID_MDC_KEY)).isNull();
    }

    @Test
    void doFilter_withNullRequestId_generatesNewId() throws IOException, ServletException {
        when(request.getHeader(RequestIdFilter.REQUEST_ID_HEADER)).thenReturn(null);

        filter.doFilter(request, response, chain);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(response).setHeader(eq(RequestIdFilter.REQUEST_ID_HEADER), captor.capture());
        assertThat(UUID.fromString(captor.getValue())).isNotNull();
        verify(chain).doFilter(request, response);
    }

    @Test
    void doFilter_withBlankRequestId_generatesNewId() throws IOException, ServletException {
        when(request.getHeader(RequestIdFilter.REQUEST_ID_HEADER)).thenReturn("   ");

        filter.doFilter(request, response, chain);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(response).setHeader(eq(RequestIdFilter.REQUEST_ID_HEADER), captor.capture());
        assertThat(UUID.fromString(captor.getValue())).isNotNull();
        verify(chain).doFilter(request, response);
    }

    @Test
    void doFilter_withEmptyRequestId_generatesNewId() throws IOException, ServletException {
        when(request.getHeader(RequestIdFilter.REQUEST_ID_HEADER)).thenReturn("");

        filter.doFilter(request, response, chain);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(response).setHeader(eq(RequestIdFilter.REQUEST_ID_HEADER), captor.capture());
        assertThat(UUID.fromString(captor.getValue())).isNotNull();
        verify(chain).doFilter(request, response);
    }

    @Test
    void doFilter_withInvalidUuid_replacesWithGenerated() throws IOException, ServletException {
        when(request.getHeader(RequestIdFilter.REQUEST_ID_HEADER)).thenReturn("not-a-valid-uuid");

        filter.doFilter(request, response, chain);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(response).setHeader(eq(RequestIdFilter.REQUEST_ID_HEADER), captor.capture());
        String generatedId = captor.getValue();
        assertThat(generatedId).isNotEqualTo("not-a-valid-uuid");
        assertThat(UUID.fromString(generatedId)).isNotNull();
        verify(chain).doFilter(request, response);
    }

    @Test
    void doFilter_setsMdcDuringChainExecution() throws IOException, ServletException {
        String providedId = UUID.randomUUID().toString();
        when(request.getHeader(RequestIdFilter.REQUEST_ID_HEADER)).thenReturn(providedId);

        doAnswer(invocation -> {
            assertThat(MDC.get(RequestIdFilter.REQUEST_ID_MDC_KEY)).isEqualTo(providedId);
            return null;
        }).when(chain).doFilter(request, response);

        filter.doFilter(request, response, chain);

        assertThat(MDC.get(RequestIdFilter.REQUEST_ID_MDC_KEY)).isNull();
    }

    @Test
    void doFilter_clearsMdcEvenWhenChainThrows() throws IOException, ServletException {
        when(request.getHeader(RequestIdFilter.REQUEST_ID_HEADER)).thenReturn(UUID.randomUUID().toString());
        doThrow(new ServletException("boom")).when(chain).doFilter(request, response);

        assertThatThrownBy(() -> filter.doFilter(request, response, chain))
                .isInstanceOf(ServletException.class)
                .hasMessage("boom");

        assertThat(MDC.get(RequestIdFilter.REQUEST_ID_MDC_KEY)).isNull();
    }

    @Test
    void constants_haveExpectedValues() {
        assertThat(RequestIdFilter.REQUEST_ID_HEADER).isEqualTo("X-Request-ID");
        assertThat(RequestIdFilter.REQUEST_ID_MDC_KEY).isEqualTo("requestId");
    }
}
