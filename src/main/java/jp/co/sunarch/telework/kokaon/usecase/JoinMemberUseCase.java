package jp.co.sunarch.telework.kokaon.usecase;

import jp.co.sunarch.telework.kokaon.event.MemberJoinedEvent;
import jp.co.sunarch.telework.kokaon.event.RoomEventPublisher;
import jp.co.sunarch.telework.kokaon.model.RoomId;
import jp.co.sunarch.telework.kokaon.model.RoomRepository;
import jp.co.sunarch.telework.kokaon.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * ルームに参加する。
 *
 * @author takeshi
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class JoinMemberUseCase {
  private final RoomRepository roomRepository;
  private final RoomEventPublisher roomEventPublisher;

  public void execute(RoomId roomId, User user) {
    var room = this.roomRepository.findById(roomId)
        .orElseThrow(() -> new RoomNotFoundException(roomId));

    if (room.isMember(user)) {
      return;
    }

    var newRoom = room.join(user);
    this.roomRepository.save(newRoom);

    log.info("Member joined: room={}, user={}", roomId.value(), user.getId().value());

    this.roomEventPublisher.publish(MemberJoinedEvent.of(roomId, user));
  }
}
