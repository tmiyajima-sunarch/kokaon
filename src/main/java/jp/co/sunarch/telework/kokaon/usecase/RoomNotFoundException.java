package jp.co.sunarch.telework.kokaon.usecase;

import jp.co.sunarch.telework.kokaon.model.RoomId;

/**
 * ルームが見つからない。
 *
 * @author takeshi
 */
public class RoomNotFoundException extends RuntimeException {
  public RoomNotFoundException(RoomId roomId) {
    super(String.format("指定されたIDのルームが見つかりません: %s", roomId.value()));
  }
}
