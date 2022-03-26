package jp.co.sunarch.telework.kokaon.model;

import org.springframework.core.io.Resource;

import java.util.Optional;

/**
 * オーディオリポジトリ。
 *
 * @author takeshi
 */
public interface AudioRepository {
  AudioId generateAudioId();

  void save(Audio audio, Resource resource);

  void deleteById(AudioId audioId);

  Optional<Audio> findById(AudioId audioId);

  Optional<byte[]> findContentById(AudioId audioId);
}
