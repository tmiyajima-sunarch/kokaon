package jp.co.sunarch.telework.kokaon.usecase;

import jp.co.sunarch.telework.kokaon.event.AudioAddedEvent;
import jp.co.sunarch.telework.kokaon.event.ClientEventPublisher;
import jp.co.sunarch.telework.kokaon.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * オーディオを追加する。
 *
 * @author takeshi
 */
@RequiredArgsConstructor
public class AddAudioUseCase {
  private final RoomRepository roomRepository;
  private final AudioRepository audioRepository;
  private final ClientEventPublisher clientEventPublisher;

  public void execute(RoomId roomId, User user, MultipartFile file, String name) {
    var room = this.roomRepository.findById(roomId)
        .orElseThrow(() -> new RoomNotFoundException(roomId));

    var audioId = this.audioRepository.generateAudioId();
    var audio = Audio.of(audioId, name);
    this.audioRepository.save(audio, file.getResource());

    var newRoom = room.addAudioBy(user, audio);
    this.roomRepository.save(newRoom);

    this.clientEventPublisher.publishRoomEvent(
        new AudioAddedEvent(newRoom.getId().value(), audio.getId().value(), audio.getName()));
  }
}
