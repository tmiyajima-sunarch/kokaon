package jp.co.sunarch.telework.kokaon.event;

import lombok.Value;

/**
 * @author takeshi
 */
@Value
public class MemberJoinedEvent implements RoomEvent {
  String roomId;
  String memberId;
  String memberNickname;
}
