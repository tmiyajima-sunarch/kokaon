package jp.co.sunarch.telework.kokaon.event;

import jp.co.sunarch.telework.kokaon.model.RoomId;
import jp.co.sunarch.telework.kokaon.model.User;
import lombok.Value;

/**
 * @author takeshi
 */
@Value
public class MemberEnteredEvent implements RoomEvent {
  String roomId;
  String memberId;
  String memberNickname;

  public static MemberEnteredEvent of(RoomId roomId, User user) {
    return new MemberEnteredEvent(roomId.value(), user.getId().value(), user.getNickname());
  }
}
