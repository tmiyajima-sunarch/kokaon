package jp.co.sunarch.telework.kokaon.event;

import jp.co.sunarch.telework.kokaon.model.AudioId;
import jp.co.sunarch.telework.kokaon.model.RoomId;
import jp.co.sunarch.telework.kokaon.model.User;
import lombok.Value;

/**
 * @author takeshi
 */
@Value
public class AudioPlayedEvent implements RoomEvent {
  String roomId;
  String audioId;
  String userId;

  public static AudioPlayedEvent of(RoomId roomId, AudioId audioId, User user) {
    return new AudioPlayedEvent(roomId.value(), audioId.value(), user.getId().value());
  }
}
