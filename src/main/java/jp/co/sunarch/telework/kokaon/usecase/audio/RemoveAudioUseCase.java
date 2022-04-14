package jp.co.sunarch.telework.kokaon.usecase.audio;

import jp.co.sunarch.telework.kokaon.event.AudioRemovedEvent;
import jp.co.sunarch.telework.kokaon.event.RoomEventPublisher;
import jp.co.sunarch.telework.kokaon.model.AudioId;
import jp.co.sunarch.telework.kokaon.model.AudioRepository;
import jp.co.sunarch.telework.kokaon.model.RoomId;
import jp.co.sunarch.telework.kokaon.model.RoomRepository;
import jp.co.sunarch.telework.kokaon.usecase.RoomNotFoundException;
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
  private final RoomEventPublisher roomEventPublisher;

  public void execute(RoomId roomId, AudioId audioId) {
    var room = this.roomRepository.findById(roomId)
        .orElseThrow(() -> new RoomNotFoundException(roomId));

    var audio = this.audioRepository.findById(audioId)
        .orElseThrow(() -> new RoomNotFoundException(roomId));
    this.audioRepository.deleteById(audioId);

    var newRoom = room.removeAudio(audio);
    this.roomRepository.save(newRoom);

    log.info("Audio removed: room={}, audio={}", roomId.value(), audioId.value());

    this.roomEventPublisher.publish(AudioRemovedEvent.of(roomId, audioId));
  }
}
