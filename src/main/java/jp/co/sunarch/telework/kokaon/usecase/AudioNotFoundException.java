package jp.co.sunarch.telework.kokaon.usecase;

import jp.co.sunarch.telework.kokaon.model.AudioId;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * オーディオが見つからない。
 *
 * @author takeshi
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class AudioNotFoundException extends RuntimeException {
  public AudioNotFoundException(AudioId audioId) {
    super(String.format("指定されたIDのオーディオが見つかりません: %s", audioId.value()));
  }
}
