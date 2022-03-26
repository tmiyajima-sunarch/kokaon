package jp.co.sunarch.telework.kokaon.controller;

import java.security.Principal;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import jp.co.sunarch.telework.kokaon.event.AudioPlayedEvent;
import jp.co.sunarch.telework.kokaon.model.Audio;
import jp.co.sunarch.telework.kokaon.model.AudioId;
import jp.co.sunarch.telework.kokaon.model.AudioRepository;
import jp.co.sunarch.telework.kokaon.model.Room;
import jp.co.sunarch.telework.kokaon.model.RoomId;
import jp.co.sunarch.telework.kokaon.model.RoomRepository;
import jp.co.sunarch.telework.kokaon.model.User;
import jp.co.sunarch.telework.kokaon.model.UserRepository;
import jp.co.sunarch.telework.kokaon.usecase.AddAudioUseCase;
import jp.co.sunarch.telework.kokaon.usecase.EnterRoomUseCase;
import jp.co.sunarch.telework.kokaon.usecase.LeaveRoomUseCase;
import lombok.Data;
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
  private final AddAudioUseCase addAudioUseCase;

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

  @PostMapping("/room/{id}/audios")
  public ResponseEntity<Void> addAudio(@PathVariable String id, AddAudioForm addAudioForm, Principal principal) {
    var roomId = new RoomId(id);
    var user = this.getUser(principal);

    // TODO バリデーション

    this.addAudioUseCase.execute(roomId, user, addAudioForm.getAudioFile(), addAudioForm.getName(), new AudioUrlResolver(roomId));

    return ResponseEntity.ok().build();
  }

  @GetMapping("/room/{id}/audios/{audioId}")
  public ResponseEntity<byte[]> getAudio(@PathVariable String id, @PathVariable String audioId) {
    var _audioId = new AudioId(audioId);
    return this.audioRepository.findContentById(_audioId)
      .map(bytes -> ResponseEntity.ok().contentType(MediaType.asMediaType(MimeType.valueOf("audio/mp3"))).body(bytes))
      .orElse(ResponseEntity.notFound().build());
  }

  @MessageMapping("/room/{id}/play/{audioId}")
  @SendTo("/topic/room/{id}")
  public AudioPlayedEvent playAudio(@DestinationVariable String id, @DestinationVariable String audioId, Principal principal) {
    var roomId = new RoomId(id);
    var _audioId = new AudioId(audioId);
    var user = this.getUser(principal);
    return AudioPlayedEvent.of(roomId, _audioId, user);
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

          return RoomJson.of(room, owner, members, audios, new AudioUrlResolver(roomId));
        });
  }

  @RequiredArgsConstructor
  private static class AudioUrlResolver implements Function<Audio, String> {
    private final RoomId roomId;

    @Override
    public String apply(Audio audio) {
      var uriComponent = UriComponentsBuilder.fromPath("/room/{id}/audios/{audioId}")
      .buildAndExpand(roomId.value(), audio.getId().value());
      return uriComponent.toUriString();
    }
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

  @Data
  static class AddAudioForm {
    String name;
    MultipartFile audioFile;
  }
}
