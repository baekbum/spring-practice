package com.example.demo.web.team.exception;

public class TeamDuplicationException extends RuntimeException {
    public TeamDuplicationException(String message) {
        super(message);
    }

  public TeamDuplicationException() {
    super();
  }

  public TeamDuplicationException(String message, Throwable cause) {
    super(message, cause);
  }

  public TeamDuplicationException(Throwable cause) {
    super(cause);
  }

  protected TeamDuplicationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
