package jp.co.sunarch.telework.kokaon.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * ルームの状態エラー。
 *
 * @author takeshi
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RoomStateException extends RuntimeException {
  public RoomStateException(String message) {
    super(message);
  }
}
