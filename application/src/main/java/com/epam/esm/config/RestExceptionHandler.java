package com.epam.esm.config;

import com.epam.esm.dto.BaseExceptionResponse;
import com.epam.esm.exception.*;
import com.epam.esm.exception.gift_certificate.InvalidCertificateException;
import com.epam.esm.exception.tag.InvalidTagException;
import com.epam.esm.exception.user.InvalidUserException;
import org.postgresql.util.PSQLException;
import org.springframework.beans.TypeMismatchException;
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
    public ResponseEntity<BaseExceptionResponse> classNotFoundExceptionHandler(ClassNotFoundException e) {
        return ResponseEntity.badRequest().body(
                new BaseExceptionResponse(HttpStatus.NOT_FOUND.value(),
                        "Internal error,Please, try again", ErrorCodeConstraints.INTERNAL_CODE));
    }


    @ExceptionHandler(PSQLException.class)
    public ResponseEntity<BaseExceptionResponse> psqlExceptionHandler(PSQLException e) {
        return ResponseEntity.badRequest().body(
                new BaseExceptionResponse(HttpStatus.BAD_REQUEST.value(),
                        "Internal error,Please, try again", ErrorCodeConstraints.INTERNAL_CODE));
    }

    @ExceptionHandler(DataAlreadyExistException.class)
    public ResponseEntity<BaseExceptionResponse> tagAlreadyExistExceptionHandler(DataAlreadyExistException e) {
        return ResponseEntity.badRequest().body(
                new BaseExceptionResponse(HttpStatus.NOT_ACCEPTABLE.value(),
                        e.getMessage(), ErrorCodeConstraints.NOT_ACCEPTABLE_CODE));
    }

    @ExceptionHandler(InvalidTagException.class)
    public ResponseEntity<BaseExceptionResponse> invalidTagExceptionHandler(InvalidTagException e) {
        return ResponseEntity.badRequest().body(
                new BaseExceptionResponse(HttpStatus.BAD_REQUEST.value(),
                        e.getMessage(), ErrorCodeConstraints.BAD_REQUEST_CODE));
    }

    @ExceptionHandler(InvalidCertificateException.class)
    public ResponseEntity<BaseExceptionResponse> invalidCertificateExceptionHandler(
            InvalidCertificateException e) {
        return ResponseEntity.badRequest().body(
                new BaseExceptionResponse(HttpStatus.BAD_REQUEST.value(),
                        e.getMessage(), ErrorCodeConstraints.BAD_REQUEST_CODE));
    }

    @ExceptionHandler(InvalidUserException.class)
    public ResponseEntity<BaseExceptionResponse> invalidUserExceptionHandler(InvalidUserException e) {
        return ResponseEntity.badRequest().body(
                new BaseExceptionResponse(HttpStatus.BAD_REQUEST.value(),
                        e.getMessage(), ErrorCodeConstraints.BAD_REQUEST_CODE));
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<BaseExceptionResponse> DataNotFoundExceptionHandler(DataNotFoundException e) {
        return ResponseEntity.status(404).body(
                new BaseExceptionResponse(HttpStatus.NOT_FOUND.value(),
                        e.getMessage(), ErrorCodeConstraints.NOT_FOUND_CODE));
    }

    @ExceptionHandler(UnknownDataBaseException.class)
    public ResponseEntity<BaseExceptionResponse> unknownDatabaseExceptionHandler(UnknownDataBaseException e) {
        return ResponseEntity.status(500).body(
                new BaseExceptionResponse(HttpStatus.CONFLICT.value(),
                        e.getMessage(), ErrorCodeConstraints.CONFLICT_CODE));
    }

    @ExceptionHandler({InvalidInputException.class})
    public ResponseEntity<BaseExceptionResponse> invalidInputException(InvalidInputException e) {
        StringBuilder message = new StringBuilder();
        e.getBindingResult().getAllErrors().forEach((error) ->
                message.append(error.getDefaultMessage()).append("   "));
        return ResponseEntity.badRequest()
                .body(new BaseExceptionResponse(HttpStatus.BAD_REQUEST.value(),
                        message.toString(), ErrorCodeConstraints.BAD_REQUEST_CODE));
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<BaseExceptionResponse> numberFormatExceptionHandler(NumberFormatException e) {
        return ResponseEntity.badRequest().body(
                new BaseExceptionResponse(HttpStatus.BAD_REQUEST.value(),
                        e.getMessage(), ErrorCodeConstraints.BAD_REQUEST_CODE));
    }

    @ExceptionHandler(BreakingDataRelationshipException.class)
    public ResponseEntity<BaseExceptionResponse> breakingDataRelationshipExceptionHandler(
            BreakingDataRelationshipException e) {
        return ResponseEntity.badRequest().body(
                new BaseExceptionResponse(HttpStatus.BAD_REQUEST.value(),
                        e.getMessage(), ErrorCodeConstraints.BAD_REQUEST_CODE));
    }

    @Override
    public ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        return ResponseEntity.badRequest().body(
                new BaseExceptionResponse(HttpStatus.BAD_REQUEST.value(),
                        "Bad request,try to send valid inputs",
                        ErrorCodeConstraints.BAD_REQUEST_CODE));
    }

    @Override
    public ResponseEntity<Object> handleTypeMismatch(
            TypeMismatchException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        return ResponseEntity.badRequest().body(
                new BaseExceptionResponse(status.value(),
                        "Not suitable input,Please, write correctly",
                        ErrorCodeConstraints.BAD_REQUEST_CODE)
        );
    }

    @Override
    public ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        return ResponseEntity.badRequest().body(
                new BaseExceptionResponse(status.value(),
                        "Missing request parameter,Please enter necessary params ",
                        ErrorCodeConstraints.BAD_REQUEST_CODE)
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<BaseExceptionResponse> constraintViolationExceptionHandler(
            ConstraintViolationException e) {
        StringBuilder message = new StringBuilder();
        e.getConstraintViolations().forEach(violation ->
                message.append(violation.getMessage()).append("    "));
        return ResponseEntity.badRequest().body(
                new BaseExceptionResponse(HttpStatus.BAD_REQUEST.value(),
                        message.toString(), ErrorCodeConstraints.BAD_REQUEST_CODE));
    }

}
