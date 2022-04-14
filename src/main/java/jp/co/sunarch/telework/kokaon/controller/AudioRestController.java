package jp.co.sunarch.telework.kokaon.controller;

import jp.co.sunarch.telework.kokaon.model.AudioId;
import jp.co.sunarch.telework.kokaon.model.RoomId;
import jp.co.sunarch.telework.kokaon.usecase.audio.RemoveAudioUseCase;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author takeshi
 */
@RestController
@CrossOrigin
@RequiredArgsConstructor
public class AudioRestController {
  private final RemoveAudioUseCase removeAudioUseCase;

  @DeleteMapping("/api/v1/room/{id}/audios/{audioId}")
  public RemoveAudioResult removeAudio(@PathVariable String id, @PathVariable String audioId) {
    this.removeAudioUseCase.execute(new RoomId(id), new AudioId(audioId));
    return new RemoveAudioResult(id, audioId);
  }

  @Value
  static class RemoveAudioResult {
    String roomId;
    String audioId;
  }
}
