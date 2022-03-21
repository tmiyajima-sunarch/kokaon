package jp.co.sunarch.telework.kokaon.model;

import java.util.Optional;

/**
 * ユーザーリポジトリ。
 *
 * @author takeshi
 */
public interface UserRepository {
  UserId generateUserId();

  void save(User user);

  Optional<User> findById(UserId userId);
}
