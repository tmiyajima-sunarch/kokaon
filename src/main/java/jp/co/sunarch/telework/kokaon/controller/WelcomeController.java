package jp.co.sunarch.telework.kokaon.controller;

import jp.co.sunarch.telework.kokaon.usecase.InitializeUserUseCase;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author takeshi
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class WelcomeController {
  private final InitializeUserUseCase initializeUserUseCase;
  private final SessionUser sessionUser;
  private final SessionPath sessionPath;

  @ModelAttribute
  public InitializeUserForm getInitializeUserForm() {
    return new InitializeUserForm();
  }

  @GetMapping("/")
  public String getIndex() {
    // すでに初期化済であればメニューに飛ばす
    if (this.sessionUser.isInitialized()) {
      log.info("すでに初期化済のユーザーのため、リダイレクトします: {}", this.sessionUser.getUser());
      return "redirect:%s".formatted(this.determineRedirectPath());
    }

    return "welcome";
  }

  @PostMapping("/initialize-user")
  public String initializeUser(InitializeUserForm initializeUserForm) {
    // すでに初期化済であればメニューに飛ばす
    if (this.sessionUser.isInitialized()) {
      log.info("すでに初期化済のユーザーのため、リダイレクトします: {}", this.sessionUser.getUser());
      return "redirect:%s".formatted(this.determineRedirectPath());
    }

    // TODO フォームのバリデーションとエラー表示

    // ユーザーを初期化して、セッションに保持する
    var user = this.initializeUserUseCase.execute(initializeUserForm.getNickname());
    this.sessionUser.setUser(user);
    log.info("ユーザーを初期化しました: {}", this.sessionUser.getUser());

    return "redirect:%s".formatted(this.determineRedirectPath());
  }

  private String determineRedirectPath() {
    // 初期化前のパスがあれば遷移する
    if (this.sessionPath.hasPath()) {
      return this.sessionPath.getAndClearPath();
    }

    return "/menu";
  }

  @Data
  static class InitializeUserForm {
    private String nickname;
  }
}
