package org.example.botservice.exception;

public class RetryableHttpException extends RuntimeException {
    public RetryableHttpException(String message) {
        super(message);
    }
}
