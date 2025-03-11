package com.example.demo.web.team.exception;

public class NoSearchTeamException extends RuntimeException {
    public NoSearchTeamException(String message) {
        super(message);
    }

    public NoSearchTeamException() {
        super();
    }

    public NoSearchTeamException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSearchTeamException(Throwable cause) {
        super(cause);
    }

    protected NoSearchTeamException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
