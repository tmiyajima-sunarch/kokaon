package jp.co.sunarch.telework.kokaon.controller;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

/**
 * @author takeshi
 */
@Component
@SessionScope
@Data
public class SessionPath {
  private String path;

  public boolean hasPath() {
    return this.path != null;
  }

  public String getAndClearPath() {
    var path = this.path;
    this.path = null;
    return path;
  }
}
