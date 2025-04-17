package com.example.demo.web.item.exception;

public class ItemDuplicationException extends RuntimeException {
    public ItemDuplicationException(String message) {
        super(message);
    }

  public ItemDuplicationException() {
    super();
  }

  public ItemDuplicationException(String message, Throwable cause) {
    super(message, cause);
  }

  public ItemDuplicationException(Throwable cause) {
    super(cause);
  }

  protected ItemDuplicationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
