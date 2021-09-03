package com.github.wenqiglantz.service.orderservice.restcontroller.advice;

import com.github.wenqiglantz.service.orderservice.data.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class GeneralExceptionControllerAdviceTest {
    private static final String UNIQUE_IDENTIFIER = "UNIQUE_IDENTIFIER";
    private static final String DESCRIPTION = "DESCRIPTION";

    @Test
    public void handleHttpRequestMethodNotSupportedException() {
        String description = "Unexpected system exception ID: " + UNIQUE_IDENTIFIER;
        HttpRequestMethodNotSupportedException exception = new HttpRequestMethodNotSupportedException(description);
        GeneralExceptionControllerAdvice advice = new GeneralExceptionControllerAdvice();
        ResponseEntity responseEntity = advice.handleHttpRequestMethodNotSupportedException(exception);
        assertThat(responseEntity.getStatusCode(), is(equalTo(HttpStatus.BAD_REQUEST)));
    }

    @Test
    public void handleRuntimeException() {
        String description = "Unexpected system exception ID: " + UNIQUE_IDENTIFIER;
        RuntimeException exception = new RuntimeException(description);
        GeneralExceptionControllerAdvice advice = new GeneralExceptionControllerAdvice();
        ResponseEntity responseEntity = advice.handleRuntimeException(exception);
        assertThat(responseEntity.getStatusCode(), is(equalTo(HttpStatus.INTERNAL_SERVER_ERROR)));
    }

    @Test
    public void handleNotFoundException() {
        NotFoundException exception = new NotFoundException(DESCRIPTION);
        GeneralExceptionControllerAdvice advice = new GeneralExceptionControllerAdvice();
        ResponseEntity responseEntity = advice.handleNotFoundException(exception);
        assertThat(responseEntity.getStatusCode(), is(equalTo(HttpStatus.NOT_FOUND)));
    }
}
