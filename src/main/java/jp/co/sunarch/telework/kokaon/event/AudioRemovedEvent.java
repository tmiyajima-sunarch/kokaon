package jp.co.sunarch.telework.kokaon.event;

import jp.co.sunarch.telework.kokaon.model.AudioId;
import jp.co.sunarch.telework.kokaon.model.RoomId;
import lombok.Value;

/**
 * @author takeshi
 */
@Value
public class AudioRemovedEvent implements RoomEvent {
  String roomId;
  String audioId;

  public static AudioRemovedEvent of(RoomId roomId, AudioId audioId) {
    return new AudioRemovedEvent(roomId.value(), audioId.value());
  }
}
