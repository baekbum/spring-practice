package com.example.demo.web.member.exception;

public class NoSearchMemberException extends RuntimeException {
    public NoSearchMemberException(String message) {
        super(message);
    }

    public NoSearchMemberException() {
        super();
    }

    public NoSearchMemberException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSearchMemberException(Throwable cause) {
        super(cause);
    }

    protected NoSearchMemberException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
