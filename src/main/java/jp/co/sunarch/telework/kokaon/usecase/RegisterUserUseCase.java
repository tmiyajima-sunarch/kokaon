package jp.co.sunarch.telework.kokaon.usecase;

import jp.co.sunarch.telework.kokaon.model.DefaultUser;
import jp.co.sunarch.telework.kokaon.model.User;
import jp.co.sunarch.telework.kokaon.model.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * ユーザーを初期化する。
 *
 * @author takeshi
 */
@RequiredArgsConstructor
@Service
public class RegisterUserUseCase {
  private final UserRepository userRepository;

  public User execute(String nickname) {
    var userId = this.userRepository.generateUserId();
    var user = DefaultUser.of(userId, nickname);
    this.userRepository.save(user);
    return user;
  }
}
