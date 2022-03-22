package jp.co.sunarch.telework.kokaon.security;

import jp.co.sunarch.telework.kokaon.model.UserId;
import lombok.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * @author takeshi
 */
@Value
public class User implements jp.co.sunarch.telework.kokaon.model.User, UserDetails {
  jp.co.sunarch.telework.kokaon.model.User wrappedUser;

  @Override
  public UserId getId() {
    return this.wrappedUser.getId();
  }

  @Override
  public String getNickname() {
    return this.wrappedUser.getNickname();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.emptyList();
  }

  @Override
  public String getPassword() {
    return "";
  }

  @Override
  public String getUsername() {
    return this.getId().value();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
