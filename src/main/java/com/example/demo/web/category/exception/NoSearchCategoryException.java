package com.example.demo.web.category.exception;

public class NoSearchCategoryException extends RuntimeException {
    public NoSearchCategoryException(String message) {
        super(message);
    }

    public NoSearchCategoryException() {
        super();
    }

    public NoSearchCategoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSearchCategoryException(Throwable cause) {
        super(cause);
    }

    protected NoSearchCategoryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
