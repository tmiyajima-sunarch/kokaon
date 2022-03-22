package jp.co.sunarch.telework.kokaon.controller;

import jp.co.sunarch.telework.kokaon.model.*;
import jp.co.sunarch.telework.kokaon.usecase.CreateRoomUseCase;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author takeshi
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class RoomController {
  private final CreateRoomUseCase createRoomUseCase;

  private final RoomRepository roomRepository;
  private final UserRepository userRepository;
  private final AudioRepository audioRepository;

  @GetMapping("/room")
  public String redirectToRoom(@RequestParam String id) {
    return "redirect:/room/%s".formatted(id);
  }

  @GetMapping("/room/{id}")
  public String getRoom(@PathVariable String id, Model model, @AuthenticationPrincipal User user) {
    var roomId = new RoomId(id);
    return this.getRoomDetail(roomId)
        .map(roomDetail -> {
          model.addAttribute("user", user);
          model.addAttribute("room", roomDetail.getRoom());
          model.addAttribute("members", roomDetail.getMembers());
          model.addAttribute("audios", roomDetail.getAudios());
          return "room";
        })
        .orElse("redirect:/");
  }

  private Optional<RoomDetail> getRoomDetail(RoomId roomId) {
    return this.roomRepository.findById(roomId)
        .map(room -> {
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

          return new RoomDetail(room, members, audios);
        });
  }

  @PostMapping("/room")
  public String createRoom(CreateRoomForm createRoomForm, @AuthenticationPrincipal User user) {
    // TODO フォームのバリデーション

    var room = this.createRoomUseCase.execute(user, createRoomForm.getName());

    return "redirect:/room/%s".formatted(room.getId().value());
  }

  @Value
  static class RoomDetail {
    Room room;
    List<User> members;
    List<Audio> audios;
  }

  @Data
  static class CreateRoomForm {
    private String name;
  }
}
