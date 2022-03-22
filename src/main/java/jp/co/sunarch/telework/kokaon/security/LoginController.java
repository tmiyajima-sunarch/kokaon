package jp.co.sunarch.telework.kokaon.security;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author takeshi
 */
@Controller
public class LoginController {
  @GetMapping("/login")
  public String getIndex() {
    return "login";
  }
}
