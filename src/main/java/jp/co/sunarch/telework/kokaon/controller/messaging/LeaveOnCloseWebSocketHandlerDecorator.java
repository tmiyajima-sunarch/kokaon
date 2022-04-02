package jp.co.sunarch.telework.kokaon.controller;

import jp.co.sunarch.telework.kokaon.model.RoomId;
import jp.co.sunarch.telework.kokaon.usecase.LeaveRoomUseCase;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @author takeshi
 */
public class LeaveOnCloseWebSocketHandlerDecorator extends WebSocketHandlerDecorator {
  private static final String SESSION_KEY = "LeaveOnCloseWebSocketHandlerDecorator";

  private final UserSessionAccessor userSessionAccessor;
  private final LeaveRoomUseCase leaveRoomUseCase;
  private final Rooms rooms = new Rooms();

  public LeaveOnCloseWebSocketHandlerDecorator(WebSocketHandler delegate, UserSessionAccessor userSessionAccessor, LeaveRoomUseCase leaveMemberUseCase) {
    super(delegate);
    this.userSessionAccessor = userSessionAccessor;
    this.leaveRoomUseCase = leaveMemberUseCase;
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    session.getAttributes().put(SESSION_KEY, this);
    super.afterConnectionEstablished(session);
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
    var user = this.userSessionAccessor.get(session.getAttributes());
    if (user != null && !this.rooms.isEmpty()) {
      this.rooms.forEach(roomId -> {
        this.leaveRoomUseCase.execute(roomId, user);
      });
    }
    super.afterConnectionClosed(session, closeStatus);
  }

  private void onEnter(RoomId roomId) {
    this.rooms.add(roomId);
  }

  private void onLeave(RoomId roomId) {
    this.rooms.remove(roomId);
  }

  public static void onEnter(Map<String, Object> sessionAttributes, RoomId roomId) {
    var self = (LeaveOnCloseWebSocketHandlerDecorator) sessionAttributes.get(SESSION_KEY);
    if (self != null) {
      self.onEnter(roomId);
    }
  }

  public static void onLeave(Map<String, Object> sessionAttributes, RoomId roomId) {
    var self = (LeaveOnCloseWebSocketHandlerDecorator) sessionAttributes.get(SESSION_KEY);
    if (self != null) {
      self.onLeave(roomId);
    }
  }

  private static class Rooms {
    private final Set<RoomId> roomIds = new HashSet<>();

    public void add(RoomId roomId) {
      this.roomIds.add(roomId);
    }

    public void remove(RoomId roomId) {
      this.roomIds.remove(roomId);
    }

    public boolean isEmpty() {
      return this.roomIds.isEmpty();
    }

    public void forEach(Consumer<RoomId> action) {
      this.roomIds.forEach(action);
    }
  }
}
