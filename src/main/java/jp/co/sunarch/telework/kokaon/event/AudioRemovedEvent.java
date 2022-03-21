package jp.co.sunarch.telework.kokaon.event;

import lombok.Value;

/**
 * @author takeshi
 */
@Value
public class AudioRemovedEvent implements RoomEvent {
  String roomId;
  String audioId;
}
