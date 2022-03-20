package jp.co.sunarch.telework.kokaon.usecase;

import jp.co.sunarch.telework.kokaon.model.Room;
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
    Room room = this.roomRepository.findById(roomId)
        .orElseThrow(() -> new RoomNotFoundException(roomId));

    Room newRoom = room.join(user);
    this.roomRepository.save(newRoom);
  }
}
