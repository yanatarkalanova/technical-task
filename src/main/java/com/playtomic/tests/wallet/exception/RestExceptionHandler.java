package com.playtomic.tests.wallet.exception;

import com.playtomic.tests.wallet.dto.StatusDto;
import com.playtomic.tests.wallet.enums.ErrorLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(RestExceptionHandler.class);

    private static final String UNKNOWN_ERROR = "UNKNOWN_ERROR";
    private static final String INVALID_REQUEST_PARAMETERS = "INVALID_REQUEST_PARAMETERS";

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> unspecifiedRuntimeErrorHandler(RuntimeException ex) {
        log.error(ex.getLocalizedMessage(), ex);
        StatusDto statusDTO = new StatusDto(UNKNOWN_ERROR, ex.getMessage(), ErrorLevel.ERROR);
        return new ResponseEntity<>(statusDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(StripeAmountTooSmallException.class)
    public ResponseEntity<Object> stripeAmountTooSmallExceptionHandler(StripeAmountTooSmallException ex) {
        log.error(ex.getLocalizedMessage(), ex);
        StatusDto statusDTO = new StatusDto(ex.getStatusCode(), ex.getMessage(), ErrorLevel.ERROR);
        return new ResponseEntity<>(statusDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> entityNotFoundExceptionHandler(EntityNotFoundException ex) {
        log.error(ex.getLocalizedMessage(), ex);
        StatusDto statusDTO = new StatusDto(ex.getStatusCode(), ex.getMessage(), ErrorLevel.ERROR);
        return new ResponseEntity<>(statusDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        BindingResult result = ex.getBindingResult();
        final List<FieldError> fieldErrors = result.getFieldErrors();
        StringBuilder errorMessage = new StringBuilder("Validation error: ");
        for (FieldError fieldError : fieldErrors) {
            errorMessage.append(fieldError.getDefaultMessage());
        }

        log.error(ex.getLocalizedMessage(), ex);
        StatusDto statusDTO = new StatusDto(INVALID_REQUEST_PARAMETERS, errorMessage.toString(), ErrorLevel.ERROR);
        return new ResponseEntity<>(statusDTO, HttpStatus.BAD_REQUEST);
    }
}
