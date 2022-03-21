package jp.co.sunarch.telework.kokaon.controller;

import jp.co.sunarch.telework.kokaon.usecase.CreateRoomUseCase;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author takeshi
 */
@Controller
@RequiredArgsConstructor
public class RoomController {
  private final SessionUser sessionUser;
  private final SessionPath sessionPath;
  private final CreateRoomUseCase createRoomUseCase;

  @GetMapping("/room")
  public String redirectToRoom(@RequestParam String id) {
    return "redirect:/room/%s".formatted(id);
  }

  @GetMapping("/room/{id}")
  public String getRoom(@PathVariable String id) {
    // ユーザーが初期化されていなければWelcomeに遷移
    if (!this.sessionUser.isInitialized()) {
      this.sessionPath.setPath("/room/%s".formatted(id));
      return "redirect:/";
    }

    return "room";
  }

  @PostMapping("/room")
  public String createRoom(CreateRoomForm createRoomForm) {
    // ユーザーが初期化されていなければWelcomeに遷移
    if (!this.sessionUser.isInitialized()) {
      this.sessionPath.setPath("/");
      return "redirect:/";
    }

    // TODO フォームのバリデーション

    var room = this.createRoomUseCase.execute(this.sessionUser.getUser(), createRoomForm.getName());

    return "redirect:/room/%s".formatted(room.getId().value());
  }

  @Data
  static class CreateRoomForm {
    private String name;
  }
}
