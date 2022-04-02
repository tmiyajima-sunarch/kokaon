package jp.co.sunarch.telework.kokaon.controller;

import jp.co.sunarch.telework.kokaon.usecase.CreateRoomUseCase;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author takeshi
 */
@Controller
@RequiredArgsConstructor
public class CreateRoomController {
  private final CreateRoomUseCase createRoomUseCase;

  @PostMapping("/room")
  public String createRoom(CreateRoomForm createRoomForm) {
    // TODO フォームのバリデーション

    var room = this.createRoomUseCase.execute(createRoomForm.getName());

    return "redirect:/room/%s".formatted(room.getId().value());
  }

  @Data
  static class CreateRoomForm {
    private String name;
  }
}
