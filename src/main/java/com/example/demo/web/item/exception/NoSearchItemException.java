package com.example.demo.web.item.exception;

public class NoSearchItemException extends RuntimeException {
    public NoSearchItemException(String message) {
        super(message);
    }

    public NoSearchItemException() {
        super();
    }

    public NoSearchItemException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSearchItemException(Throwable cause) {
        super(cause);
    }

    protected NoSearchItemException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
