package jp.co.sunarch.telework.kokaon.controller;

import jp.co.sunarch.telework.kokaon.model.Audio;
import jp.co.sunarch.telework.kokaon.model.RoomId;
import lombok.RequiredArgsConstructor;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.function.Function;

/**
 * @author takeshi
 */
@RequiredArgsConstructor
public class AudioUrlResolver implements Function<Audio, String> {
  private final RoomId roomId;

  @Override
  public String apply(Audio audio) {
    var uriComponent = UriComponentsBuilder.fromPath("/room/{id}/audios/{audioId}")
        .buildAndExpand(roomId.value(), audio.getId().value());
    return uriComponent.toUriString();
  }

}
