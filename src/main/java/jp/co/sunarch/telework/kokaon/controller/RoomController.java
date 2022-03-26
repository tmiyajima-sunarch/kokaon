package jp.co.sunarch.telework.kokaon.controller;

import java.security.Principal;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.sunarch.telework.kokaon.model.Audio;
import jp.co.sunarch.telework.kokaon.model.AudioRepository;
import jp.co.sunarch.telework.kokaon.model.Room;
import jp.co.sunarch.telework.kokaon.model.RoomId;
import jp.co.sunarch.telework.kokaon.model.RoomRepository;
import jp.co.sunarch.telework.kokaon.model.User;
import jp.co.sunarch.telework.kokaon.model.UserRepository;
import jp.co.sunarch.telework.kokaon.usecase.EnterRoomUseCase;
import jp.co.sunarch.telework.kokaon.usecase.LeaveRoomUseCase;
import lombok.RequiredArgsConstructor;
import lombok.Value;

/**
 * @author takeshi
 */
@Controller
@RequiredArgsConstructor
public class RoomController {
  private final RoomRepository roomRepository;
  private final UserRepository userRepository;
  private final AudioRepository audioRepository;

  private final EnterRoomUseCase enterRoomUseCase;
  private final LeaveRoomUseCase leaveRoomUseCase;

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

  @SubscribeMapping("/room/{id}")
  public AppJson onSubscribe(@DestinationVariable String id, Principal principal) {
    var roomId = new RoomId(id);
    var user = this.getUser(principal);

    var room = this.getRoomJson(roomId).get();
    return new AppJson(UserJson.of(user), room);
  }

  @MessageMapping("/room/{id}/enter")
  public void enter(@DestinationVariable String id, SimpMessageHeaderAccessor headerAccessor, Principal principal) {
    var roomId = new RoomId(id);
    var user = this.getUser(principal);

    LeaveOnCloseWebSocketHandlerDecorator.onEnter(
        Objects.requireNonNull(headerAccessor.getSessionAttributes()), roomId);

    this.enterRoomUseCase.execute(roomId, user);
  }

  @MessageMapping("/room/{id}/leave")
  public void leave(@DestinationVariable String id, SimpMessageHeaderAccessor headerAccessor, Principal principal) {
    var roomId = new RoomId(id);
    var user = this.getUser(principal);

    LeaveOnCloseWebSocketHandlerDecorator.onLeave(
        Objects.requireNonNull(headerAccessor.getSessionAttributes()), roomId);

    this.leaveRoomUseCase.execute(roomId, user);
  }

  private User getUser(Principal principal) {
    var auth = (Authentication) principal;
    return (User) auth.getPrincipal();
  }

  private Optional<RoomJson> getRoomJson(RoomId roomId) {
    return this.roomRepository.findById(roomId)
        .map(room -> {
          var owner = this.userRepository.findById(room.getOwnerId()).get();
          var members = room.getMemberIds().stream()
              .flatMap(memberId -> this.userRepository.findById(memberId).stream())
              .sorted(Comparator.comparing(User::getNickname))
              .toList();
          var audios = room.getAudioIds().stream()
              .flatMap(audioId -> this.audioRepository.findById(audioId).stream())
              .sorted(Comparator.comparing(Audio::getName))
              .toList();

          return RoomJson.of(room, owner, members, audios, (audio) -> "TODO");
        });
  }

  @Value
  static class AppJson {
    UserJson me;
    RoomJson room;
  }

  @Value
  static class RoomJson {
    String id;
    String name;
    UserJson owner;
    List<UserJson> members;
    List<AudioJson> audios;

    static RoomJson of(Room room, User owner, Collection<User> members, Collection<Audio> audios,
        Function<Audio, String> toUrl) {
      return new RoomJson(
          room.getId().value(),
          room.getName(),
          UserJson.of(owner),
          members.stream().map(UserJson::of).toList(),
          audios.stream().map(audio -> AudioJson.of(audio, toUrl.apply(audio))).toList());
    }
  }

  @Value
  static class UserJson {
    String id;
    String nickname;

    static UserJson of(User user) {
      return new UserJson(user.getId().value(), user.getNickname());
    }
  }

  @Value
  static class AudioJson {
    String id;
    String name;
    String url;

    static AudioJson of(Audio audio, String url) {
      return new AudioJson(audio.getId().value(), audio.getName(), url);
    }
  }
}
