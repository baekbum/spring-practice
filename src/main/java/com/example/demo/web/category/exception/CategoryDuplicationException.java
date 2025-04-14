package com.example.demo.web.category.exception;

public class CategoryDuplicationException extends RuntimeException {
    public CategoryDuplicationException(String message) {
        super(message);
    }

  public CategoryDuplicationException() {
    super();
  }

  public CategoryDuplicationException(String message, Throwable cause) {
    super(message, cause);
  }

  public CategoryDuplicationException(Throwable cause) {
    super(cause);
  }

  protected CategoryDuplicationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
