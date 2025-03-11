package com.example.demo.web.member.exception;

public class ParameterBingingException extends RuntimeException {
    public ParameterBingingException(String message) {
          super(message);
      }

    public ParameterBingingException() {
      super();
    }

    public ParameterBingingException(String message, Throwable cause) {
      super(message, cause);
    }

    public ParameterBingingException(Throwable cause) {
      super(cause);
    }

    protected ParameterBingingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
      super(message, cause, enableSuppression, writableStackTrace);
    }
}
