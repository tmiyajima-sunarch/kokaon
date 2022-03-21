package jp.co.sunarch.telework.kokaon.usecase;

import jp.co.sunarch.telework.kokaon.event.AudioRemovedEvent;
import jp.co.sunarch.telework.kokaon.event.ClientEventPublisher;
import jp.co.sunarch.telework.kokaon.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * オーディオを削除する。
 *
 * @author takeshi
 */
@RequiredArgsConstructor
@Service
public class RemoveAudioUseCase {
  private final RoomRepository roomRepository;
  private final AudioRepository audioRepository;
  private final ClientEventPublisher clientEventPublisher;

  public void execute(RoomId roomId, User user, AudioId audioId) {
    var room = this.roomRepository.findById(roomId)
        .orElseThrow(() -> new RoomNotFoundException(roomId));

    var audio = this.audioRepository.findById(audioId)
        .orElseThrow(() -> new RoomNotFoundException(roomId));
    this.audioRepository.deleteById(audioId);

    var newRoom = room.removeAudioBy(user, audio);
    this.roomRepository.save(newRoom);

    this.clientEventPublisher.publishRoomEvent(
        new AudioRemovedEvent(newRoom.getId().value(), audio.getId().value()));
  }
}
