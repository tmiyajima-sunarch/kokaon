package jp.co.sunarch.telework.kokaon.model;

/**
 * ユーザーリポジトリ。
 *
 * @author takeshi
 */
public interface UserRepository {
  UserId generateUserId();

  void save(User user);
}
