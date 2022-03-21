package jp.co.sunarch.telework.kokaon.usecase;

import jp.co.sunarch.telework.kokaon.model.Room;
import jp.co.sunarch.telework.kokaon.model.RoomRepository;
import jp.co.sunarch.telework.kokaon.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * ルームを作成する。
 *
 * @author takeshi
 */
@RequiredArgsConstructor
@Service
public class CreateRoomUseCase {
  private final RoomRepository roomRepository;

  public Room execute(User owner, String name) {
    var roomId = this.roomRepository.generateRoomId();
    var room = Room.of(roomId, name, owner);
    this.roomRepository.save(room);
    return room;
  }
}
