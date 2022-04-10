package jp.co.sunarch.telework.kokaon.controller.messaging;

import jp.co.sunarch.telework.kokaon.controller.AudioUrlResolver;
import jp.co.sunarch.telework.kokaon.event.AudioPlayedEvent;
import jp.co.sunarch.telework.kokaon.model.*;
import jp.co.sunarch.telework.kokaon.usecase.EnterRoomUseCase;
import jp.co.sunarch.telework.kokaon.usecase.LeaveRoomUseCase;
import jp.co.sunarch.telework.kokaon.usecase.ValidateRoomPassCodeUseCase;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.*;
import java.util.function.Function;

/**
 * @author takeshi
 */
@Controller
@RequiredArgsConstructor
public class RoomMessagingController {
  private final RoomRepository roomRepository;
  private final AudioRepository audioRepository;

  private final ValidateRoomPassCodeUseCase validateRoomPassCodeUseCase;
  private final EnterRoomUseCase enterRoomUseCase;
  private final LeaveRoomUseCase leaveRoomUseCase;

  private final UserSessionAccessor userSessionAccessor;

  @SubscribeMapping("/room/{id}")
  public RoomJson onSubscribe(@DestinationVariable String id, StompHeaderAccessor stompHeaderAccessor) {
    var roomId = new RoomId(id);
    var passcode = stompHeaderAccessor.getNativeHeader("passcode").get(0);
    this.validateRoomPassCodeUseCase.execute(roomId, passcode);
    return this.getRoomJson(roomId).get();
  }

  @MessageMapping("/room/{id}/enter")
  public void enter(@DestinationVariable String id, EnterForm enterForm, SimpMessageHeaderAccessor headerAccessor) {
    var roomId = new RoomId(id);
    var user = User.of(new UserId(enterForm.getId()), enterForm.getNickname());

    this.enterRoomUseCase.execute(roomId, user);

    LeaveOnCloseWebSocketHandlerDecorator.onEnter(
        Objects.requireNonNull(headerAccessor.getSessionAttributes()), roomId);

    this.userSessionAccessor.set(headerAccessor.getSessionAttributes(), user);
  }

  @MessageMapping("/room/{id}/leave")
  public void leave(@DestinationVariable String id, SimpMessageHeaderAccessor headerAccessor) {
    var roomId = new RoomId(id);
    var user = this.userSessionAccessor.get(headerAccessor.getSessionAttributes());

    this.leaveRoomUseCase.execute(roomId, user);

    LeaveOnCloseWebSocketHandlerDecorator.onLeave(
        Objects.requireNonNull(headerAccessor.getSessionAttributes()), roomId);
  }

  @MessageMapping("/room/{id}/play/{audioId}")
  @SendTo("/topic/room/{id}")
  public AudioPlayedEvent playAudio(@DestinationVariable String id, @DestinationVariable String audioId, SimpMessageHeaderAccessor headerAccessor) {
    var roomId = new RoomId(id);
    var _audioId = new AudioId(audioId);
    var user = this.userSessionAccessor.get(headerAccessor.getSessionAttributes());
    return AudioPlayedEvent.of(roomId, _audioId, user);
  }

  private Optional<RoomJson> getRoomJson(RoomId roomId) {
    return this.roomRepository.findById(roomId)
        .map(room -> {
          var members = room.getMembers().stream()
              .sorted(Comparator.comparing(User::getNickname))
              .toList();
          var audios = room.getAudioIds().stream()
              .flatMap(audioId -> this.audioRepository.findById(audioId).stream())
              .sorted(Comparator.comparing(Audio::getName))
              .toList();

          return RoomJson.of(room, members, audios, new AudioUrlResolver(roomId));
        });
  }

  @Value
  static class RoomJson {
    String id;
    String passcode;
    String name;
    List<UserJson> members;
    List<AudioJson> audios;

    static RoomJson of(Room room, Collection<User> members, Collection<Audio> audios, Function<Audio, String> toUrl) {
      return new RoomJson(
          room.getId().value(),
          room.getPassCode().value(),
          room.getName(),
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
  static class EnterForm {
    String id;
    String nickname;
  }

}
