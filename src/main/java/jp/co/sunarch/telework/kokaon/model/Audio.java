package jp.co.sunarch.telework.kokaon.model;

import lombok.Value;

/**
 * オーディオ。
 *
 * @author takeshi
 */
@Value
public class Audio {
  AudioId id;
  String name;

  public static Audio of(AudioId id, String name) {
    return new Audio(id, name);
  }
}
