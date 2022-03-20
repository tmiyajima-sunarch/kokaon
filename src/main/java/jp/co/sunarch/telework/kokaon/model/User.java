package jp.co.sunarch.telework.kokaon.model;

import lombok.Value;

/**
 * ユーザー。
 *
 * @author takeshi
 */
@Value
public class User {
  UserId id;
  String nickname;
}
