package com.pm.patientservice.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

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
    public ResponseEntity<Map<String, String>> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex, HttpServletRequest request) {

        logException(ex);
        Map<String, String> errorResponse = errorResponseBody(
                HttpStatus.BAD_REQUEST.toString(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                request.getRequestURI(),
                ex.getMessage()
        );

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(PatientNotFoundException.class)
    public ResponseEntity<Map<String, String>> handlePatientNotFoundException(PatientNotFoundException ex, HttpServletRequest request) {

        logException(ex);
        Map<String, String> errorResponse = errorResponseBody(
                HttpStatus.NOT_FOUND.toString(),
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

    private Map<String, String> errorResponseBody(String status, String reasonPhrase, String path, String message) {
        Map<String, String> error = new HashMap<>();
        error.put("status", status);
        error.put("error", reasonPhrase);
        error.put("path", path);
        error.put("message", message);
        error.put("timestamp", String.valueOf(System.currentTimeMillis()));
        return error;
    }
}
