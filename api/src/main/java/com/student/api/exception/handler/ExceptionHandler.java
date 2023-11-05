package com.student.api.exception.handler;

import com.student.api.exception.*;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.MethodParameter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String DELETION_FORBIDDEN_MESSAGE = "Deletion of given object is forbidden because is used by other related object(s).";
    @org.springframework.web.bind.annotation.ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDTO> handleNotFoundException(NotFoundException ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorDTO error = buildErrorDTO(ex.getMessage(), status);
        return new ResponseEntity<>(error, status);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ObjectAlreadyExistsException.class)
    public ResponseEntity<ErrorDTO> handleObjectAlreadyExistsException(ObjectAlreadyExistsException ex) {
        HttpStatus status = HttpStatus.CONFLICT;
        ErrorDTO error = buildErrorDTO(ex.getMessage(), status);
        return new ResponseEntity<>(error, status);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDTO> handleDeletionForbiddenException(DataIntegrityViolationException ex) {
        HttpStatus status = HttpStatus.CONFLICT;
        ErrorDTO error = buildErrorDTO(DELETION_FORBIDDEN_MESSAGE, status);
        return new ResponseEntity<>(error, status);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(IncorrectFormatException.class)
    public ResponseEntity<ErrorDTO> handleIncorrectFormatExceptionException(IncorrectFormatException ex) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        ErrorDTO error = buildErrorDTO(ex.getMessage(), status);
        return new ResponseEntity<>(error, status);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(IncorrectValueException.class)
    public ResponseEntity<ErrorDTO> handleIncorrectValueExceptionException(IncorrectValueException ex) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        ErrorDTO error = buildErrorDTO(ex.getMessage(), status);
        return new ResponseEntity<>(error, status);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(CancellationForbiddenException.class)
    public ResponseEntity<ErrorDTO> handleCancellationForbiddenException(CancellationForbiddenException ex) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        ErrorDTO error = buildErrorDTO(ex.getMessage(), status);
        return new ResponseEntity<>(error, status);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(TokenInfoExtractionException.class)
    public ResponseEntity<ErrorDTO> handleTokenInfoExtractionException(TokenInfoExtractionException ex) {
        ErrorDTO error = buildErrorDTO(ex.getMessage(), ex.getStatus());
        return new ResponseEntity<>(error, ex.getStatus());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(DentalClinicException.class)
    public ResponseEntity<ErrorDTO> handleDentalClinicException(DentalClinicException ex) {
        ErrorDTO error = buildErrorDTO(ex.getMessage(), ex.getStatus());
        return new ResponseEntity<>(error, ex.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ErrorDTO error = new ErrorDTO();
        error.setReasons(
                Optional.of(ex)
                .map(MethodArgumentNotValidException::getFieldErrors)
                .stream()
                .flatMap(List::stream)
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toSet())
        );
        error.setStatus(status.value());
        error.setMessage(
                String.format(
                        "Błąd walidacji klasy %s",
                        Optional.of(ex)
                        .map(MethodArgumentNotValidException::getParameter)
                                .map(MethodParameter::getParameter)
                                .map(Parameter::getAnnotatedType)
                                .map(AnnotatedType::getType)
                                .map(Type::getTypeName)
                                .orElse("")
                )
        );
        return new ResponseEntity<>(error, status);
    }

    private ErrorDTO buildErrorDTO(String message, HttpStatus status) {
        return ErrorDTO.builder()
                .message(message)
                .status(status.value())
                .reasons(Collections.singleton(status.getReasonPhrase()))
                .build();
    }
}
