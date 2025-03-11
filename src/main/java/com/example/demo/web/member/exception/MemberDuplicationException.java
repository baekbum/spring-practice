package com.example.demo.web.member.exception;

public class MemberDuplicationException extends RuntimeException {
    public MemberDuplicationException(String message) {
        super(message);
    }

  public MemberDuplicationException() {
    super();
  }

  public MemberDuplicationException(String message, Throwable cause) {
    super(message, cause);
  }

  public MemberDuplicationException(Throwable cause) {
    super(cause);
  }

  protected MemberDuplicationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
