package jp.co.sunarch.telework.kokaon.model;

import jp.co.sunarch.telework.kokaon.util.Sets;
import lombok.AccessLevel;
import lombok.Value;
import lombok.With;

import java.util.Set;

/**
 * ルーム。
 *
 * @author takeshi
 */
@Value
public class Room {
  RoomId id;
  PassCode passCode;
  String name;
  @With(AccessLevel.PRIVATE)
  RoomState state;
  @With(AccessLevel.PRIVATE)
  Set<User> members;
  @With(AccessLevel.PRIVATE)
  Set<AudioId> audioIds;

  public static Room of(RoomId id, PassCode passCode, String name) {
    return new Room(id, passCode, name, RoomState.OPEN, Set.of(), Set.of());
  }

  public Room enter(User user) {
    this.checkStateIsOpen();
    return this.withMembers(Sets.add(this.members, user));
  }

  public Room leave(User user) {
    this.checkStateIsOpen();
    return this.withMembers(Sets.remove(this.members, user));
  }

  public Room addAudio(Audio audio) {
    this.checkStateIsOpen();
    return this.withAudioIds(Sets.add(this.audioIds, audio.getId()));
  }

  public Room removeAudio(Audio audio) {
    this.checkStateIsOpen();
    return this.withAudioIds(Sets.remove(this.audioIds, audio.getId()));
  }

  public Room closeBy(User user) {
    this.checkStateIsOpen();
    return this.withState(RoomState.CLOSED);
  }

  public boolean isMember(User user) {
    return this.members.contains(user);
  }

  public boolean isPassCodeMatches(String passCode) {
    return this.passCode.matches(passCode);
  }

  private void checkStateIsOpen() {
    if (this.state == RoomState.CLOSED) {
      throw new RoomStateException("ルームは既にクローズされています: %s".formatted(this.id.value()));
    }
  }

}
