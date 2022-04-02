package jp.co.sunarch.telework.kokaon.usecase;

import jp.co.sunarch.telework.kokaon.model.AudioId;
import jp.co.sunarch.telework.kokaon.model.AudioRepository;
import jp.co.sunarch.telework.kokaon.model.RoomId;
import jp.co.sunarch.telework.kokaon.model.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * オーディオを削除する。
 *
 * @author takeshi
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class RemoveAudioUseCase {
  private final RoomRepository roomRepository;
  private final AudioRepository audioRepository;

  public void execute(RoomId roomId, AudioId audioId) {
    var room = this.roomRepository.findById(roomId)
        .orElseThrow(() -> new RoomNotFoundException(roomId));

    var audio = this.audioRepository.findById(audioId)
        .orElseThrow(() -> new RoomNotFoundException(roomId));
    this.audioRepository.deleteById(audioId);

    var newRoom = room.removeAudio(audio);
    this.roomRepository.save(newRoom);

    log.info("Audio removed: room={}, audio={}", roomId.value(), audioId.value());
  }
}
