package jp.co.sunarch.telework.kokaon.event;

import lombok.Value;

/**
 * @author takeshi
 */
@Value
public class RoomClosedEvent implements RoomEvent {
  String roomId;
}
