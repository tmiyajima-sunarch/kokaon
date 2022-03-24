package jp.co.sunarch.telework.kokaon.event;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * ルームに関するイベント。
 *
 * @author takeshi
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
public interface RoomEvent {
  String getRoomId();
}
