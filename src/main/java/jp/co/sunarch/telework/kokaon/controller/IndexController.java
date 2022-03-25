package jp.co.sunarch.telework.kokaon.controller;

import jp.co.sunarch.telework.kokaon.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author takeshi
 */
@Controller
@RequiredArgsConstructor
public class IndexController {
  @GetMapping("/")
  public String getMenu(Model model, @AuthenticationPrincipal User user) {
    model.addAttribute("user", user);

    return "index";
  }
}
