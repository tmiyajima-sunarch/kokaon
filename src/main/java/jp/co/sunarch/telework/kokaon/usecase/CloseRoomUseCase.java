package jp.co.sunarch.telework.kokaon.usecase;

import jp.co.sunarch.telework.kokaon.event.ClientEventPublisher;
import jp.co.sunarch.telework.kokaon.event.RoomClosedEvent;
import jp.co.sunarch.telework.kokaon.model.RoomId;
import jp.co.sunarch.telework.kokaon.model.RoomRepository;
import jp.co.sunarch.telework.kokaon.model.User;
import lombok.RequiredArgsConstructor;

/**
 * ルームを閉じる。
 *
 * @author takeshi
 */
@RequiredArgsConstructor
public class CloseRoomUseCase {
  private final RoomRepository roomRepository;
  private final ClientEventPublisher clientEventPublisher;

  public void execute(RoomId roomId, User user) {
    var room = this.roomRepository.findById(roomId)
        .orElseThrow(() -> new RoomNotFoundException(roomId));

    var newRoom = room.closeBy(user);
    this.roomRepository.save(newRoom);

    this.clientEventPublisher.publishRoomEvent(
        new RoomClosedEvent(newRoom.getId().value()));
  }
}
