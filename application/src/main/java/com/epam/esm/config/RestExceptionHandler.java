package com.epam.esm.config;

import com.epam.esm.dto.BaseExceptionResponse;
import com.epam.esm.exception.*;
import com.epam.esm.exception.gift_certificate.InvalidCertificateException;
import com.epam.esm.exception.tag.InvalidTagException;
import com.epam.esm.exception.user.InvalidUserException;
import org.postgresql.util.PSQLException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ClassNotFoundException.class)
    public ResponseEntity<BaseExceptionResponse> classNotFoundExceptionHandler(Exception e) {
        return ResponseEntity.ok(
                new BaseExceptionResponse(404, e.getLocalizedMessage(), 50002));
    }

    @ExceptionHandler(PSQLException.class)
    public ResponseEntity<BaseExceptionResponse> psqlExceptionHandler(PSQLException e) {
        return ResponseEntity.status(400).body(
                new BaseExceptionResponse(400, e.getLocalizedMessage(), 50001));
    }

    @ExceptionHandler(DataAlreadyExistException.class)
    public ResponseEntity<BaseExceptionResponse> tagAlreadyExistExceptionHandler(DataAlreadyExistException e) {
        return ResponseEntity.badRequest().body(
                new BaseExceptionResponse(400, e.getMessage(), 40002));
    }

    @ExceptionHandler(InvalidTagException.class)
    public ResponseEntity<BaseExceptionResponse> invalidTagExceptionHandler(InvalidTagException e) {
        return ResponseEntity.badRequest().body(
                new BaseExceptionResponse(400, e.getMessage(), 40001));
    }

    @ExceptionHandler(InvalidCertificateException.class)
    public ResponseEntity<BaseExceptionResponse> invalidCertificateExceptionHandler(
            InvalidCertificateException e) {
        return ResponseEntity.badRequest().body(
                new BaseExceptionResponse(400, e.getMessage(), 40001));
    }

    @ExceptionHandler(InvalidUserException.class)
    public ResponseEntity<BaseExceptionResponse> invalidUserExceptionHandler(InvalidUserException e) {
        return ResponseEntity.badRequest().body(
                new BaseExceptionResponse(400, e.getMessage(), 40001));
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<BaseExceptionResponse> noDataFoundExceptionHandler(DataNotFoundException e) {
        return ResponseEntity.status(400).body(
                new BaseExceptionResponse(404, e.getMessage(), 40401));
    }

    @ExceptionHandler(UnknownDataBaseException.class)
    public ResponseEntity<BaseExceptionResponse> unknownDatabaseExceptionHandler(UnknownDataBaseException e) {
        return ResponseEntity.status(500).body(
                new BaseExceptionResponse(400, e.getMessage(), 40002));
    }

    @ExceptionHandler({InvalidInputException.class})
    public ResponseEntity<BaseExceptionResponse> invalidInputException(InvalidInputException e) {
        StringBuilder message = new StringBuilder();
        e.getBindingResult().getAllErrors().forEach((error) ->
                message.append(error.getDefaultMessage()).append("   "));
        return ResponseEntity.badRequest()
                .body(new BaseExceptionResponse(400, message.toString(), 40001));
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<BaseExceptionResponse> numberFormatExceptionHandler(NumberFormatException e) {
        return ResponseEntity.status(400).body(
                new BaseExceptionResponse(400, e.getMessage(), 40001));
    }

    @ExceptionHandler(BreakingDataRelationshipException.class)
    public ResponseEntity<BaseExceptionResponse> breakingDataRelationshipExceptionHandler(
            BreakingDataRelationshipException e) {
        return ResponseEntity.status(400).body(
                new BaseExceptionResponse(400, e.getMessage(), 40001));
    }
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        return ResponseEntity.status(400).body(
                new BaseExceptionResponse(400, ex.getCause().getMessage(), 40001));
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(
            org.springframework.beans.TypeMismatchException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        return ResponseEntity.status(400).body(
                new BaseExceptionResponse(status.value(), ex.getMessage(), 40001)
        );
    }
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        return ResponseEntity.status(400).body(
                new BaseExceptionResponse(status.value(), ex.getMessage(), 10400)
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<BaseExceptionResponse> constraintViolationExceptionHandler(
            ConstraintViolationException e) {
        StringBuilder message = new StringBuilder();
        e.getConstraintViolations().forEach(violation ->
                message.append(violation.getMessage()).append("    "));
        return ResponseEntity.status(400).body(
                new BaseExceptionResponse(400, message.toString(), 40001));
    }

}
