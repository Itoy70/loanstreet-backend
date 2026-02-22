package com.loanstreet.backend.exception;

import com.loanstreet.backend.dto.ApiError;
import com.loanstreet.backend.dto.FieldViolation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void handleValidationErrors_returnsBadRequestWithFieldViolations() {
        FieldError fieldError1 = new FieldError("loan", "amount", "must be positive");
        FieldError fieldError2 = new FieldError("loan", "interestRate", "must not be null");

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError1, fieldError2));

        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<ApiError> response = handler.handleValidationErrors(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        ApiError body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getError()).isEqualTo("Validation failed");
        assertThat(body.getTimestamp()).isNotNull();

        List<FieldViolation> errors = body.getErrors();
        assertThat(errors).hasSize(2);
        assertThat(errors.get(0).getField()).isEqualTo("amount");
        assertThat(errors.get(0).getMessage()).isEqualTo("must be positive");
        assertThat(errors.get(1).getField()).isEqualTo("interestRate");
        assertThat(errors.get(1).getMessage()).isEqualTo("must not be null");
    }

    @Test
    void handleValidationErrors_emptyFieldErrors_returnsBadRequestWithEmptyList() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(List.of());

        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<ApiError> response = handler.handleValidationErrors(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getErrors()).isEmpty();
    }

    @Test
    void handleNotFound_returnsNotFoundWithMessage() {
        NoSuchElementException ex = new NoSuchElementException("Loan not found");

        ResponseEntity<ApiError> response = handler.handleNotFound(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        ApiError body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getError()).isEqualTo("Resource not found");
        assertThat(body.getTimestamp()).isNotNull();
    }

    @Test
    void handleTypeMismatch_returnsBadRequest() {
        MethodArgumentTypeMismatchException ex = mock(MethodArgumentTypeMismatchException.class);
        when(ex.getName()).thenReturn("id");
        when(ex.getValue()).thenReturn("not-a-uuid");

        ResponseEntity<ApiError> response = handler.handleTypeMismatch(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        ApiError body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getError()).isEqualTo("Invalid parameter format");
        assertThat(body.getTimestamp()).isNotNull();
    }

    @Test
    void handleGenericException_returnsInternalServerError() {
        Exception ex = new RuntimeException("Something went wrong");

        ResponseEntity<ApiError> response = handler.handleGenericException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        ApiError body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getError()).isEqualTo("An unexpected error occurred");
        assertThat(body.getTimestamp()).isNotNull();
    }
}
