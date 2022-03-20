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
  String name;
  User owner;
  @With(AccessLevel.PRIVATE)
  RoomState state;
  @With(AccessLevel.PRIVATE)
  Set<User> members;
  @With(AccessLevel.PRIVATE)
  Set<Audio> audioSet;

  public static Room of(RoomId id, String name, User owner) {
    return new Room(id, name, owner, RoomState.OPEN, Set.of(), Set.of());
  }

  public Room join(User user) {
    this.checkStateIsOpen();
    return this.withMembers(Sets.add(this.members, user));
  }

  public Room leave(User user) {
    this.checkStateIsOpen();
    return this.withMembers(Sets.remove(this.members, user));
  }

  public Room addAudioBy(User user, Audio audio) {
    this.checkStateIsOpen();
    this.checkUserIsMember(user);
    return this.withAudioSet(Sets.add(this.audioSet, audio));
  }

  public Room removeAudioBy(User user, Audio audio) {
    this.checkStateIsOpen();
    this.checkUserIsMember(user);
    return this.withAudioSet(Sets.remove(this.audioSet, audio));
  }

  public Room closeBy(User user) {
    this.checkStateIsOpen();
    this.checkUserIsOwner(user);
    return this.withState(RoomState.CLOSED);
  }

  private void checkStateIsOpen() {
    if (this.state == RoomState.CLOSED) {
      throw new RoomStateException("ルームは既にクローズされています: %s".formatted(this.id.value()));
    }
  }

  private void checkUserIsMember(User user) {
    if (!this.owner.equals(user) && !this.members.contains(user)) {
      throw new RoomOperationException("ルームメンバー以外には許可されていません: %s".formatted(this.id.value()));
    }
  }

  private void checkUserIsOwner(User user) {
    if (!this.owner.equals(user)) {
      throw new RoomOperationException("ルームオーナー以外には許可されていません: %s".formatted(this.id.value()));
    }
  }

}
