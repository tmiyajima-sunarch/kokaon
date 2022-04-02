package jp.co.sunarch.telework.kokaon.usecase;

import jp.co.sunarch.telework.kokaon.event.AudioAddedEvent;
import jp.co.sunarch.telework.kokaon.event.RoomEventPublisher;
import jp.co.sunarch.telework.kokaon.model.Audio;
import jp.co.sunarch.telework.kokaon.model.AudioRepository;
import jp.co.sunarch.telework.kokaon.model.RoomId;
import jp.co.sunarch.telework.kokaon.model.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.function.Function;

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
  private final RoomEventPublisher roomEventPublisher;

  public void execute(RoomId roomId, MultipartFile file, String name, Function<Audio, String> toUrl) {
    var room = this.roomRepository.findById(roomId)
        .orElseThrow(() -> new RoomNotFoundException(roomId));

    var audioId = this.audioRepository.generateAudioId();
    var audio = Audio.of(audioId, name);
    this.audioRepository.save(audio, file.getResource());

    var newRoom = room.addAudio(audio);
    this.roomRepository.save(newRoom);

    log.info("Audio added: room={}, audio={}", roomId.value(), audioId.value());

    this.roomEventPublisher.publish(AudioAddedEvent.of(roomId, audio, toUrl.apply(audio)));
  }
}
