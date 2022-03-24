package jp.co.sunarch.telework.kokaon.usecase;

import jp.co.sunarch.telework.kokaon.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * オーディオを追加する。
 *
 * @author takeshi
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class AddAudioUseCase {
  private final RoomRepository roomRepository;
  private final AudioRepository audioRepository;

  public void execute(RoomId roomId, User user, MultipartFile file, String name) {
    var room = this.roomRepository.findById(roomId)
        .orElseThrow(() -> new RoomNotFoundException(roomId));

    var audioId = this.audioRepository.generateAudioId();
    var audio = Audio.of(audioId, name);
    this.audioRepository.save(audio, file.getResource());

    var newRoom = room.addAudioBy(user, audio);
    this.roomRepository.save(newRoom);

    log.info("Audio added: room={}, audio={}, user={}", roomId.value(), audioId.value(), user.getId().value());
  }
}
