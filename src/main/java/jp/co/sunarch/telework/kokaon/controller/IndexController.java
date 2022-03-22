package jp.co.sunarch.telework.kokaon.controller;

import jp.co.sunarch.telework.kokaon.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author takeshi
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class MenuController {
  @GetMapping("/")
  public String getMenu(Model model, @AuthenticationPrincipal User user) {
    model.addAttribute("user", user);

    return "index";
  }
}
