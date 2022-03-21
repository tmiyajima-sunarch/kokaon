package jp.co.sunarch.telework.kokaon.usecase;

import jp.co.sunarch.telework.kokaon.model.RoomId;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * ルームが見つからない。
 *
 * @author takeshi
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class RoomNotFoundException extends RuntimeException {
  public RoomNotFoundException(RoomId roomId) {
    super(String.format("指定されたIDのルームが見つかりません: %s", roomId.value()));
  }
}
