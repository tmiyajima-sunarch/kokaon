package jp.co.sunarch.telework.kokaon.usecase;

import jp.co.sunarch.telework.kokaon.model.RoomId;
import jp.co.sunarch.telework.kokaon.model.RoomRepository;
import jp.co.sunarch.telework.kokaon.model.User;
import lombok.RequiredArgsConstructor;

/**
 * ルームに参加する。
 *
 * @author takeshi
 */
@RequiredArgsConstructor
public class JoinRoomUseCase {
  private final RoomRepository roomRepository;

  public void execute(RoomId roomId, User user) {
    var room = this.roomRepository.findById(roomId)
        .orElseThrow(() -> new RoomNotFoundException(roomId));

    var newRoom = room.join(user);
    this.roomRepository.save(newRoom);
  }
}
