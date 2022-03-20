package jp.co.sunarch.telework.kokaon.model;

/**
 * ルームの状態エラー。
 *
 * @author takeshi
 */
public class RoomStateException extends RuntimeException {
  public RoomStateException(String message) {
    super(message);
  }
}
