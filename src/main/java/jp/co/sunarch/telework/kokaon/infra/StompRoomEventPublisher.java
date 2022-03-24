package jp.co.sunarch.telework.kokaon.infra;

import jp.co.sunarch.telework.kokaon.event.RoomEvent;
import jp.co.sunarch.telework.kokaon.event.RoomEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

/**
 * @author takeshi
 */
@RequiredArgsConstructor
@Component
public class StompRoomEventPublisher implements RoomEventPublisher {
  private final SimpMessagingTemplate template;

  @Override
  public void publish(RoomEvent event) {
    this.template.convertAndSend("/topic/room/%s".formatted(event.getRoomId()), event);
  }
}
