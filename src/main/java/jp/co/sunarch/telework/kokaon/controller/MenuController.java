package jp.co.sunarch.telework.kokaon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author takeshi
 */
@Controller
@RequiredArgsConstructor
public class MenuController {
  private final SessionUser sessionUser;
  private final SessionPath sessionPath;

  @GetMapping("/menu")
  public String getMenu(Model model) {
    // ユーザーが初期化されていなければWelcomeに遷移
    if (!this.sessionUser.isInitialized()) {
      this.sessionPath.setPath("/menu");
      return "redirect:/";
    }

    model.addAttribute("user", sessionUser.getUser());

    return "menu";
  }
}
