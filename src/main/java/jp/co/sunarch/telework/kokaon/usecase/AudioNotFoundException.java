package jp.co.sunarch.telework.kokaon.usecase;

import jp.co.sunarch.telework.kokaon.model.AudioId;

/**
 * オーディオが見つからない。
 *
 * @author takeshi
 */
public class AudioNotFoundException extends RuntimeException {
  public AudioNotFoundException(AudioId audioId) {
    super(String.format("指定されたIDのオーディオが見つかりません: %s", audioId.value()));
  }
}
