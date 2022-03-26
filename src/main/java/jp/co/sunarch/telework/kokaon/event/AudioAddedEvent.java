package jp.co.sunarch.telework.kokaon.event;

import jp.co.sunarch.telework.kokaon.model.Audio;
import jp.co.sunarch.telework.kokaon.model.RoomId;
import lombok.Value;

/**
 * @author takeshi
 */
@Value
public class AudioAddedEvent implements RoomEvent {
  String roomId;
  String audioId;
  String audioName;
  String audioUrl;

  public static AudioAddedEvent of(RoomId roomId, Audio audio, String url) {
    return new AudioAddedEvent(roomId.value(), audio.getId().value(), audio.getName(), url);
  }
}
