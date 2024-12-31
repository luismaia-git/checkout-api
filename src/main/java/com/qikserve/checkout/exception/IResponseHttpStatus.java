package com.qikserve.checkout.exception;

import org.springframework.http.HttpStatus;

public interface IResponseHttpStatus {
    public HttpStatus getHttpStatus();
}
