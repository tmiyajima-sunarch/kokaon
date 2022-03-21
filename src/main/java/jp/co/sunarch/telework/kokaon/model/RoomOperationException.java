package jp.co.sunarch.telework.kokaon.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * ルームの操作エラー。
 *
 * @author takeshi
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class RoomOperationException extends RuntimeException {
  public RoomOperationException(String message) {
    super(message);
  }
}
