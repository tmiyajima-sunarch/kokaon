package jp.co.sunarch.telework.kokaon.infra;

import jp.co.sunarch.telework.kokaon.event.ClientEventPublisher;
import jp.co.sunarch.telework.kokaon.event.RoomEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;

/**
 * STOMPを使って、イベントを通知する。
 *
 * @author takeshi
 */
@RequiredArgsConstructor
public class StompClientEventPublisher implements ClientEventPublisher {
  private final SimpMessagingTemplate simpMessagingTemplate;

  @Override
  public void publishRoomEvent(RoomEvent event) {
    this.simpMessagingTemplate.convertAndSend("/topic/rooms/%s".formatted(event.getRoomId()), event);
  }
}
