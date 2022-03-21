package jp.co.sunarch.telework.kokaon.event;

/**
 * クライアントにイベントを通知する。
 *
 * @author takeshi
 */
public interface ClientEventPublisher {
  void publishRoomEvent(RoomEvent event);
}
