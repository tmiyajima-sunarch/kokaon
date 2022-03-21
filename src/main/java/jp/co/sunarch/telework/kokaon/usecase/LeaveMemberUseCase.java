package jp.co.sunarch.telework.kokaon.usecase;

import jp.co.sunarch.telework.kokaon.event.ClientEventPublisher;
import jp.co.sunarch.telework.kokaon.event.MemberLeftEvent;
import jp.co.sunarch.telework.kokaon.model.RoomId;
import jp.co.sunarch.telework.kokaon.model.RoomRepository;
import jp.co.sunarch.telework.kokaon.model.User;
import lombok.RequiredArgsConstructor;

/**
 * ルームから退室する。
 *
 * @author takeshi
 */
@RequiredArgsConstructor
public class LeaveMemberUseCase {
  private final RoomRepository roomRepository;
  private final ClientEventPublisher clientEventPublisher;

  public void execute(RoomId roomId, User user) {
    var room = this.roomRepository.findById(roomId)
        .orElseThrow(() -> new RoomNotFoundException(roomId));

    var newRoom = room.leave(user);
    this.roomRepository.save(newRoom);

    this.clientEventPublisher.publishRoomEvent(
        new MemberLeftEvent(newRoom.getId().value(), user.getId().value()));
  }
}
