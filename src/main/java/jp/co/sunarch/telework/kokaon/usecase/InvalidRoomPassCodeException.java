package jp.co.sunarch.telework.kokaon.usecase;

import jp.co.sunarch.telework.kokaon.model.RoomId;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * パスコードが違う。
 *
 * @author takeshi
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidRoomPassCodeException extends RuntimeException {
  public InvalidRoomPassCodeException(RoomId roomId) {
    super(String.format("ルームのパスコードが一致しません: %s", roomId.value()));
  }
}
