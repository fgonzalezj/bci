package com.bci.users.exceptions;

import com.fasterxml.jackson.annotation.JsonAlias;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.ZonedDateTime;
import lombok.Getter;
import org.springframework.util.Assert;

@Getter
public class ExceptionDetail implements Serializable {
  private static final long serialVersionUID = 1905128741950251207L;

  @JsonAlias({"code", "code"})
  private int code;

  @JsonAlias({"detail", "detail"})
  private String detail;

  @JsonAlias({"timestamp", "timestamp"})
  private ZonedDateTime timestamp;

  public ExceptionDetail() {}

  public ExceptionDetail(int code, String detail, ZonedDateTime timestamp) {
    Assert.notNull(code, "error code must not be null");
    Assert.notNull(detail, "detail must not be null");
    Assert.notNull(timestamp, "timestamp must not be null");
    this.code = code;
    this.detail = detail;
    this.timestamp = timestamp;
  }

  private void readObject(ObjectInputStream aInputStream)
      throws ClassNotFoundException, IOException {
    aInputStream.defaultReadObject();
  }

  private void writeObject(ObjectOutputStream aOutputStream) throws IOException {
    aOutputStream.defaultWriteObject();
  }
}
