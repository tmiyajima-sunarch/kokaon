package jp.co.sunarch.telework.kokaon.event;

/**
 * @author takeshi
 */
public interface RoomEventPublisher {
  void publish(RoomEvent event);
}
