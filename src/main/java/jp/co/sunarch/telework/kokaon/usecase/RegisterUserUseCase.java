package jp.co.sunarch.telework.kokaon.usecase;

import jp.co.sunarch.telework.kokaon.model.DefaultUser;
import jp.co.sunarch.telework.kokaon.model.User;
import jp.co.sunarch.telework.kokaon.model.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * ユーザーを初期化する。
 *
 * @author takeshi
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class RegisterUserUseCase {
  private final UserRepository userRepository;

  public User execute(String nickname) {
    var userId = this.userRepository.generateUserId();
    var user = DefaultUser.of(userId, nickname);
    this.userRepository.save(user);

    log.info("User registered: user={}", user.getId().value());

    return user;
  }
}
