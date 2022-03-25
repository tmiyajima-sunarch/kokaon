package jp.co.sunarch.telework.kokaon.controller;

import jp.co.sunarch.telework.kokaon.model.*;
import jp.co.sunarch.telework.kokaon.usecase.JoinMemberUseCase;
import jp.co.sunarch.telework.kokaon.usecase.LeaveMemberUseCase;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author takeshi
 */
@Controller
@RequiredArgsConstructor
public class RoomController {
  private final RoomRepository roomRepository;
  private final UserRepository userRepository;
  private final AudioRepository audioRepository;

  private final JoinMemberUseCase joinMemberUseCase;
  private final LeaveMemberUseCase leaveMemberUseCase;

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

  @MessageMapping("/room/{id}/join")
  public void join(@DestinationVariable String id, SimpMessageHeaderAccessor headerAccessor, Principal principal) {
    var roomId = new RoomId(id);
    var user = this.getUser(principal);

    LeaveOnCloseWebSocketHandlerDecorator.onJoin(
        Objects.requireNonNull(headerAccessor.getSessionAttributes()), roomId);

    this.joinMemberUseCase.execute(roomId, user);
  }

  @MessageMapping("/room/{id}/leave")
  public void leave(@DestinationVariable String id, SimpMessageHeaderAccessor headerAccessor, Principal principal) {
    var roomId = new RoomId(id);
    var user = this.getUser(principal);

    LeaveOnCloseWebSocketHandlerDecorator.onLeave(
        Objects.requireNonNull(headerAccessor.getSessionAttributes()), roomId);

    this.leaveMemberUseCase.execute(roomId, user);
  }

  private User getUser(Principal principal) {
    var auth = (Authentication) principal;
    return (User) auth.getPrincipal();
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

  @Value
  static class RoomDetail {
    Room room;
    List<User> members;
    List<Audio> audios;
  }
}
