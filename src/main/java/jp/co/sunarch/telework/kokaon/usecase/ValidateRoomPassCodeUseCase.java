package jp.co.sunarch.telework.kokaon.usecase;

import jp.co.sunarch.telework.kokaon.model.RoomId;
import jp.co.sunarch.telework.kokaon.model.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * パスコードを検証する。
 *
 * @author takeshi
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class ValidateRoomPassCodeUseCase {
  private final RoomRepository roomRepository;

  public void execute(RoomId roomId, String passCode) {
    var room = this.roomRepository.findById(roomId)
        .orElseThrow(() -> new RoomNotFoundException(roomId));

    if (!room.isPassCodeMatches(passCode)) {
      throw new InvalidRoomPassCodeException(roomId);
    }
  }
}
