package jp.co.sunarch.telework.kokaon.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.WebSocketSession;

/**
 * @author takeshi
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class RoomMessageController {
  private final WebSocketSession session;

  @MessageMapping("/room/{id}/join")
  public void handleJoin(@DestinationVariable String id) {
    log.info("session: {}", session.getAttributes());
  }
}
