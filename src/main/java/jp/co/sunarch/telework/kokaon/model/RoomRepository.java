package jp.co.sunarch.telework.kokaon.model;

import java.util.Optional;

/**
 * ルームリポジトリ。
 *
 * @author takeshi
 */
public interface RoomRepository {

  /**
   * ルームIDを採番する。
   */
  RoomId generateRoomId();

  /**
   * ルームを保存する。
   */
  void save(Room room);

  /**
   * ルームをIDで取得する。
   */
  Optional<Room> findById(RoomId roomId);

}
