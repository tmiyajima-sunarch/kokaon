package jp.co.sunarch.telework.kokaon.model;

import lombok.Value;

/**
 * @author takeshi
 */
@Value
public class DefaultUser implements User {
  UserId id;
  String nickname;

  public static DefaultUser of(UserId id, String nickname) {
    return new DefaultUser(id, nickname);
  }
}
