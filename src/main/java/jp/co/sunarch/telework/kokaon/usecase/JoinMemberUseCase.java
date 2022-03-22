package jp.co.sunarch.telework.kokaon.usecase;

import jp.co.sunarch.telework.kokaon.model.RoomId;
import jp.co.sunarch.telework.kokaon.model.RoomRepository;
import jp.co.sunarch.telework.kokaon.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * ルームに参加する。
 *
 * @author takeshi
 */
@RequiredArgsConstructor
@Service
public class JoinMemberUseCase {
  private final RoomRepository roomRepository;

  public boolean execute(RoomId roomId, User user) {
    var room = this.roomRepository.findById(roomId)
        .orElseThrow(() -> new RoomNotFoundException(roomId));

    if (room.isMember(user)) {
      return false;
    }

    var newRoom = room.join(user);
    this.roomRepository.save(newRoom);

    return true;
  }
}
