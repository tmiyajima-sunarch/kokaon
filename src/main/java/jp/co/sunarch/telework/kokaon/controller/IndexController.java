package jp.co.sunarch.telework.kokaon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author takeshi
 */
@Controller
@RequiredArgsConstructor
public class IndexController {
  @GetMapping("/")
  public String getMenu() {
    return "index";
  }
}
