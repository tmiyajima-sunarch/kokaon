package jp.co.sunarch.telework.kokaon.controller;

import jp.co.sunarch.telework.kokaon.model.AudioRepository;
import jp.co.sunarch.telework.kokaon.model.RoomId;
import jp.co.sunarch.telework.kokaon.model.RoomRepository;
import jp.co.sunarch.telework.kokaon.usecase.AddAudioUseCase;
import jp.co.sunarch.telework.kokaon.usecase.CreateRoomUseCase;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
  private final RoomRepository roomRepository;
  private final AudioRepository audioRepository;
  private final CreateRoomUseCase createRoomUseCase;
  private final AddAudioUseCase addAudioUseCase;

  @PostMapping("/room")
  public String createRoom(CreateRoomForm createRoomForm) {
    // TODO フォームのバリデーション

    var room = this.createRoomUseCase.execute(createRoomForm.getName());

    return "redirect:/room/%s".formatted(room.getId().value());
  }

  @GetMapping("/room")
  public String redirectToRoom(@RequestParam String id) {
    return "redirect:/room/%s".formatted(id);
  }

  @GetMapping("/room/{id}")
  public String getRoom(@PathVariable String id, Model model) {
    var roomId = new RoomId(id);
    return this.roomRepository.findById(roomId)
        .map(room -> {
          model.addAttribute("room", room);
          return "room";
        }).orElse("redirect:/");
  }

  @Data
  static class CreateRoomForm {
    private String name;
  }
}
