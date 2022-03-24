package jp.co.sunarch.telework.kokaon.event;

import jp.co.sunarch.telework.kokaon.model.RoomId;
import jp.co.sunarch.telework.kokaon.model.User;
import lombok.Value;

/**
 * @author takeshi
 */
@Value
public class MemberJoinedEvent implements RoomEvent {
  String roomId;
  String memberId;
  String memberNickname;

  public static MemberJoinedEvent of(RoomId roomId, User user) {
    return new MemberJoinedEvent(roomId.value(), user.getId().value(), user.getNickname());
  }
}
