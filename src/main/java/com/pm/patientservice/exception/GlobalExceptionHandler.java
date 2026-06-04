package com.pm.patientservice.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        logException(ex);
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex, HttpServletRequest request) {

        logException(ex);
        Map<String, Object> errorResponse = errorResponseBody(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                request.getRequestURI(),
                ex.getMessage()
        );

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(PatientNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handlePatientNotFoundException(PatientNotFoundException ex, HttpServletRequest request) {

        logException(ex);
        Map<String, Object> errorResponse = errorResponseBody(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                request.getRequestURI(),
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    private void logException(Exception ex) {

        StackTraceElement element = ex.getStackTrace()[2];

        log.warn(
                "Exception in {}.{}() at line {} -> {}",
                element.getClassName(),
                element.getMethodName(),
                element.getLineNumber(),
                ex.getMessage()
        );
    }

    private Map<String, Object> errorResponseBody(int status, String reasonPhrase, String path, String message) {
        Map<String, Object> error = new HashMap<>();
        error.put("status", status);
        error.put("error", reasonPhrase);
        error.put("path", path);
        error.put("message", message);
        error.put("timestamp", OffsetDateTime.now(ZoneOffset.UTC).toString());
        return error;
    }
}
