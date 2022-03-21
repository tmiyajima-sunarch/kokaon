package jp.co.sunarch.telework.kokaon.controller;

import jp.co.sunarch.telework.kokaon.model.User;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

/**
 * @author takeshi
 */
@Component
@SessionScope
@Data
public class SessionUser {
  private User user;

  public boolean isInitialized() {
    return this.user != null;
  }
}
