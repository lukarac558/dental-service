package com.student.reservationservice.common.exception.handler;

import com.student.api.ErrorDTO;
import com.student.reservationservice.common.exception.entity.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CommonExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String DELETION_FORBIDDEN_MESSAGE = "Deletion of given object is forbidden because is used by other related object(s).";
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDTO> handleNotFoundException(NotFoundException ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorDTO error = buildErrorDTO(ex.getMessage(), status);
        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(ObjectAlreadyExistsException.class)
    public ResponseEntity<ErrorDTO> handleObjectAlreadyExistsException(ObjectAlreadyExistsException ex) {
        HttpStatus status = HttpStatus.CONFLICT;
        ErrorDTO error = buildErrorDTO(ex.getMessage(), status);
        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDTO> handleDeletionForbiddenException(DataIntegrityViolationException ex) {
        HttpStatus status = HttpStatus.CONFLICT;
        ErrorDTO error = buildErrorDTO(DELETION_FORBIDDEN_MESSAGE, status);
        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(IncorrectFormatException.class)
    public ResponseEntity<ErrorDTO> handleIncorrectFormatExceptionException(IncorrectFormatException ex) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        ErrorDTO error = buildErrorDTO(ex.getMessage(), status);
        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(IncorrectValueException.class)
    public ResponseEntity<ErrorDTO> handleIncorrectValueExceptionException(IncorrectValueException ex) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        ErrorDTO error = buildErrorDTO(ex.getMessage(), status);
        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(CancellationForbiddenException.class)
    public ResponseEntity<ErrorDTO> handleCancellationForbiddenException(CancellationForbiddenException ex) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        ErrorDTO error = buildErrorDTO(ex.getMessage(), status);
        return new ResponseEntity<>(error, status);
    }

    private ErrorDTO buildErrorDTO(String message, HttpStatus status) {
        return ErrorDTO.builder()
                .message(message)
                .status(status.value())
                .reason(status.getReasonPhrase())
                .build();
    }
}
