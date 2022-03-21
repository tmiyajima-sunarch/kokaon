package jp.co.sunarch.telework.kokaon.infra;

import jp.co.sunarch.telework.kokaon.model.Room;
import jp.co.sunarch.telework.kokaon.model.RoomId;
import jp.co.sunarch.telework.kokaon.model.RoomRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ルームリポジトリのインメモリ実装。
 *
 * @author takeshi
 */
@Repository
public class InMemoryRoomRepository implements RoomRepository {
  private final Map<RoomId, Room> rooms = new ConcurrentHashMap<>();

  @Override
  public RoomId generateRoomId() {
    return new RoomId(UUID.randomUUID().toString());
  }

  @Override
  public void save(Room room) {
    this.rooms.put(room.getId(), room);
  }

  @Override
  public Optional<Room> findById(RoomId roomId) {
    return Optional.ofNullable(this.rooms.get(roomId));
  }
}
