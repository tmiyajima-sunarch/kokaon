package jp.co.sunarch.telework.kokaon.controller;

import jp.co.sunarch.telework.kokaon.model.*;
import jp.co.sunarch.telework.kokaon.usecase.CreateRoomUseCase;
import jp.co.sunarch.telework.kokaon.usecase.RoomNotFoundException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.stream.Stream;

/**
 * @author takeshi
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class RoomController {
  private final SessionUser sessionUser;
  private final SessionPath sessionPath;
  private final CreateRoomUseCase createRoomUseCase;

  private final RoomRepository roomRepository;
  private final UserRepository userRepository;
  private final AudioRepository audioRepository;

  @GetMapping("/room")
  public String redirectToRoom(@RequestParam String id) {
    return "redirect:/room/%s".formatted(id);
  }

  @GetMapping("/room/{id}")
  public String getRoom(@PathVariable String id, Model model) {
    // ユーザーが初期化されていなければWelcomeに遷移
    if (!this.sessionUser.isInitialized()) {
      log.info("ユーザーが初期化されていないため、リダイレクトします");
      this.sessionPath.setPath("/room/%s".formatted(id));
      return "redirect:/";
    }

    var roomId = new RoomId(id);
    var room = this.roomRepository.findById(roomId)
        .orElseThrow(() -> new RoomNotFoundException(roomId));

    var members = Stream.concat(
            this.userRepository.findById(room.getOwnerId()).stream(),
            room.getMemberIds().stream()
                .flatMap(memberId -> this.userRepository.findById(memberId).stream())
                .sorted(Comparator.comparing(User::getNickname)))
        .toList();

    var audios = room.getAudioIds().stream()
        .flatMap(audioId -> this.audioRepository.findById(audioId).stream())
        .sorted(Comparator.comparing(Audio::getName))
        .toList();

    model.addAttribute("user", this.sessionUser.getUser());
    model.addAttribute("room", room);
    model.addAttribute("members", members);
    model.addAttribute("audios", audios);

    return "room";
  }

  @PostMapping("/room")
  public String createRoom(CreateRoomForm createRoomForm) {
    // ユーザーが初期化されていなければWelcomeに遷移
    if (!this.sessionUser.isInitialized()) {
      log.info("ユーザーが初期化されていないため、リダイレクトします");
      this.sessionPath.setPath("/menu");
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
