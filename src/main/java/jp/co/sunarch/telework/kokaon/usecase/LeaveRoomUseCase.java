package jp.co.sunarch.telework.kokaon.usecase;

import jp.co.sunarch.telework.kokaon.event.MemberLeftEvent;
import jp.co.sunarch.telework.kokaon.event.RoomEventPublisher;
import jp.co.sunarch.telework.kokaon.model.RoomId;
import jp.co.sunarch.telework.kokaon.model.RoomRepository;
import jp.co.sunarch.telework.kokaon.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * ルームから退室する。
 *
 * @author takeshi
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class LeaveRoomUseCase {
  private final RoomRepository roomRepository;
  private final RoomEventPublisher roomEventPublisher;

  public void execute(RoomId roomId, User user) {
    var room = this.roomRepository.findById(roomId)
        .orElseThrow(() -> new RoomNotFoundException(roomId));

    if (!room.isMember(user)) {
      return;
    }

    var newRoom = room.leave(user);
    this.roomRepository.save(newRoom);

    log.info("Member left: room={}, user={}", roomId.value(), user.getId().value());

    this.roomEventPublisher.publish(MemberLeftEvent.of(roomId, user));
  }
}
