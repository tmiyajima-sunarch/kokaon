package jp.co.sunarch.telework.kokaon.security;

import jp.co.sunarch.telework.kokaon.usecase.RegisterUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * @author takeshi
 */
@Component
@RequiredArgsConstructor
public class InitializingUserDetailsService implements UserDetailsService {
  private final RegisterUserUseCase registerUserUseCase;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    jp.co.sunarch.telework.kokaon.model.User user = this.registerUserUseCase.execute(username);
    return new User(user);
  }
}
