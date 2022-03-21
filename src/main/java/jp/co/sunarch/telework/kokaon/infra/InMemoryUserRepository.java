package jp.co.sunarch.telework.kokaon.infra;

import jp.co.sunarch.telework.kokaon.model.User;
import jp.co.sunarch.telework.kokaon.model.UserId;
import jp.co.sunarch.telework.kokaon.model.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ユーザーリポジトリのインメモリ実装。
 *
 * @author takeshi
 */
@Repository
public class InMemoryUserRepository implements UserRepository {
  private final Map<UserId, User> users = new ConcurrentHashMap<>();

  @Override
  public UserId generateUserId() {
    return new UserId(UUID.randomUUID().toString());
  }

  @Override
  public void save(User user) {
    this.users.put(user.getId(), user);
  }
}
