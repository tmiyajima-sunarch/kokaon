package jp.co.sunarch.telework.kokaon.infra;

import jp.co.sunarch.telework.kokaon.model.Audio;
import jp.co.sunarch.telework.kokaon.model.AudioId;
import jp.co.sunarch.telework.kokaon.model.AudioRepository;
import lombok.SneakyThrows;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * オーディオリポジトリのインメモリ実装。
 *
 * @author takeshi
 */
@Repository
public class InMemoryAudioRepository implements AudioRepository {
  private final Map<AudioId, Audio> audios = new ConcurrentHashMap<>();
  private final Map<AudioId, byte[]> resources = new ConcurrentHashMap<>();

  @Override
  public AudioId generateAudioId() {
    return new AudioId(UUID.randomUUID().toString());
  }

  @SneakyThrows
  @Override
  public void save(Audio audio, Resource resource) {
    this.audios.put(audio.getId(), audio);
    this.resources.put(audio.getId(), resource.getInputStream().readAllBytes());
  }

  @Override
  public void deleteById(AudioId audioId) {
    this.audios.remove(audioId);
    this.resources.remove(audioId);
  }

  @Override
  public Optional<Audio> findById(AudioId audioId) {
    return Optional.ofNullable(this.audios.get(audioId));
  }

  @Override
  public Optional<byte[]> findContentById(AudioId audioId) {
    return Optional.ofNullable(this.resources.get(audioId));
  }
}
