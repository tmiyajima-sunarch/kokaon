package jp.co.sunarch.telework.kokaon.controller;

import jp.co.sunarch.telework.kokaon.model.AudioId;
import jp.co.sunarch.telework.kokaon.model.AudioRepository;
import jp.co.sunarch.telework.kokaon.model.RoomId;
import jp.co.sunarch.telework.kokaon.usecase.audio.AddAudioUseCase;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author takeshi
 */
@Controller
@CrossOrigin
@RequiredArgsConstructor
public class AudioController {
  private final AddAudioUseCase addAudioUseCase;
  private final AudioRepository audioRepository;

  @PostMapping("/room/{id}/audios")
  public ResponseEntity<AddAudioResult> addAudio(@PathVariable String id, AddAudioForm addAudioForm) {
    var roomId = new RoomId(id);

    // TODO バリデーション

    var audioId = this.addAudioUseCase.execute(roomId, addAudioForm.getAudioFile(), addAudioForm.getName(), new AudioUrlResolver(roomId));

    return ResponseEntity.ok(new AddAudioResult(roomId.value(), audioId.value(), addAudioForm.getName()));
  }

  @GetMapping("/room/{id}/audios/{audioId}")
  public ResponseEntity<byte[]> getAudio(@PathVariable String id, @PathVariable String audioId) {
    var _audioId = new AudioId(audioId);
    return this.audioRepository.findContentById(_audioId)
        .map(bytes -> ResponseEntity.ok().contentType(MediaType.asMediaType(MimeType.valueOf("audio/mp3"))).body(bytes))
        .orElse(ResponseEntity.notFound().build());
  }

  @Data
  static class AddAudioForm {
    String name;
    MultipartFile audioFile;
  }

  @Value
  static class AddAudioResult {
    String roomId;
    String audioId;
    String audioName;
  }
}
