package jp.co.sunarch.telework.kokaon.model;

/**
 * ルームの操作エラー。
 *
 * @author takeshi
 */
public class RoomOperationException extends RuntimeException {
  public RoomOperationException(String message) {
    super(message);
  }
}
