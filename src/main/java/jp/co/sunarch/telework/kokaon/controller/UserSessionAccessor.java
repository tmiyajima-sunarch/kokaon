package jp.co.sunarch.telework.kokaon.controller;

import jp.co.sunarch.telework.kokaon.model.User;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * WebSocketのSessionAttributesに対するユーザー情報の入出力をする。
 *
 * @author takeshi
 */
@Component
public class UserSessionAccessor {

  public static final String KEY = User.class.getName();

  @Nullable
  public User get(Map<String, Object> sessionAttributes) {
    return (User) sessionAttributes.get(KEY);
  }

  public void set(Map<String, Object> sessionAttributes, User user) {
    sessionAttributes.put(KEY, user);
  }

  public void remove(Map<String, Object> sessionAttributes) {
    sessionAttributes.remove(KEY);
  }

}
