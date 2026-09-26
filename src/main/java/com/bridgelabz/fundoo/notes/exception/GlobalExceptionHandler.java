package com.bridgelabz.fundoo.notes.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ErrorResponse> build(HttpStatus status, String message, HttpServletRequest request) {

        return new ResponseEntity<>(
                new ErrorResponse(
                        LocalDateTime.now(),
                        status.value(),
                        message,
                        request.getRequestURI()
                ),
                status
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> userNotFound(UserNotFoundException e, HttpServletRequest request) {
        return build(
                HttpStatus.NOT_FOUND,
                e.getMessage(),
                request
        );
    }

    @ExceptionHandler(NoteNotFoundException.class)
    public ResponseEntity<ErrorResponse> noteNotFound(NoteNotFoundException e, HttpServletRequest request) {
        return build(
                HttpStatus.NOT_FOUND,
                e.getMessage(),
                request
        );
    }

    @ExceptionHandler(LabelNotFoundException.class)
    public ResponseEntity<ErrorResponse> labelNotFound(LabelNotFoundException e, HttpServletRequest request) {
        return build(
                HttpStatus.NOT_FOUND,
                e.getMessage(),
                request
        );
    }

    @ExceptionHandler(ReminderNotFoundException.class)
    public ResponseEntity<ErrorResponse> reminderNotFound(ReminderNotFoundException e, HttpServletRequest request) {
        return build(
                HttpStatus.NOT_FOUND,
                e.getMessage(),
                request
        );
    }

    @ExceptionHandler(AttachmentNotFoundException.class)
    public ResponseEntity<ErrorResponse> attachmentNotFound(AttachmentNotFoundException e, HttpServletRequest request) {
        return build(
                HttpStatus.NOT_FOUND,
                e.getMessage(),
                request
        );
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ErrorResponse> duplicateEmail(DuplicateEmailException e, HttpServletRequest request) {
        return build(
                HttpStatus.CONFLICT,
                e.getMessage(),
                request
        );
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ErrorResponse> invalidPassword(InvalidPasswordException e, HttpServletRequest request) {
        return build(
                HttpStatus.UNAUTHORIZED,
                e.getMessage(),
                request
        );
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorResponse> invalidToken(InvalidTokenException e, HttpServletRequest request) {
        return build(
                HttpStatus.UNAUTHORIZED,
                e.getMessage(),
                request
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> validationError(MethodArgumentNotValidException e, HttpServletRequest request) {
        String message = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("Invalid request data");

        return build(
                HttpStatus.BAD_REQUEST,
                message,
                request
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> illegalArgument(IllegalArgumentException e, HttpServletRequest request) {
        return build(
                HttpStatus.BAD_REQUEST,
                e.getMessage(),
                request
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> generalException(Exception e, HttpServletRequest request) {
        return build(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal server error",
                request
        );
    }
}