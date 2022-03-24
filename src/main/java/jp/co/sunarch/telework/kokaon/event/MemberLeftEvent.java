package jp.co.sunarch.telework.kokaon.event;

import jp.co.sunarch.telework.kokaon.model.RoomId;
import jp.co.sunarch.telework.kokaon.model.User;
import lombok.Value;

/**
 * @author takeshi
 */
@Value
public class MemberLeftEvent implements RoomEvent {
  String roomId;
  String memberId;

  public static MemberLeftEvent of(RoomId roomId, User user) {
    return new MemberLeftEvent(roomId.value(), user.getId().value());
  }
}
