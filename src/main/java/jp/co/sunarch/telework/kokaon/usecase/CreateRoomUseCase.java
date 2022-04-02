package jp.co.sunarch.telework.kokaon.usecase;

import jp.co.sunarch.telework.kokaon.model.Room;
import jp.co.sunarch.telework.kokaon.model.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * ルームを作成する。
 *
 * @author takeshi
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class CreateRoomUseCase {
  private final RoomRepository roomRepository;

  public Room execute(String name) {
    var roomId = this.roomRepository.generateRoomId();
    var room = Room.of(roomId, name);
    this.roomRepository.save(room);

    log.info("Room created: room={}", roomId.value());

    return room;
  }
}
