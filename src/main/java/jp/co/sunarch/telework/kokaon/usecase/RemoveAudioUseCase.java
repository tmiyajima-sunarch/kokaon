package jp.co.sunarch.telework.kokaon.usecase;

import jp.co.sunarch.telework.kokaon.model.*;
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

  public void execute(RoomId roomId, User user, AudioId audioId) {
    var room = this.roomRepository.findById(roomId)
        .orElseThrow(() -> new RoomNotFoundException(roomId));

    var audio = this.audioRepository.findById(audioId)
        .orElseThrow(() -> new RoomNotFoundException(roomId));
    this.audioRepository.deleteById(audioId);

    var newRoom = room.removeAudioBy(user, audio);
    this.roomRepository.save(newRoom);

    log.info("Audio removed: room={}, audio={}, user={}", roomId.value(), audioId.value(), user.getId().value());
  }
}
