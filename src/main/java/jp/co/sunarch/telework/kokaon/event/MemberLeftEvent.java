package jp.co.sunarch.telework.kokaon.event;

import lombok.Value;

/**
 * @author takeshi
 */
@Value
public class MemberLeftEvent implements RoomEvent {
  String roomId;
  String memberId;
}
