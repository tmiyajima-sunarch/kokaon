package jp.co.sunarch.telework.kokaon.model;

import java.util.HashSet;
import java.util.Set;

/**
 * ルーム。
 *
 * @author takeshi
 */
public record Room(
    RoomId id,
    String name,
    User owner,
    Set<User> members,
    RoomState state
) {
  public static Room of(RoomId id, String name, User owner) {
    return new Room(id, name, owner, Set.of(), RoomState.OPEN);
  }

  public Room join(User user) {
    this.checkStateIsOpen();
    var newMembers = new HashSet<User>(this.members);
    newMembers.add(user);
    return new Room(this.id, this.name, this.owner, Set.copyOf(newMembers), this.state);
  }

  public Room leave(User user) {
    this.checkStateIsOpen();
    var newMembers = new HashSet<User>(this.members);
    newMembers.remove(user);
    return new Room(this.id, this.name, this.owner, Set.copyOf(newMembers), this.state);
  }

  public Room closeBy(User user) {
    this.checkStateIsOpen();
    this.checkUserIsOwner(user);

    return new Room(this.id, this.name, this.owner, this.members, RoomState.CLOSED);
  }

  private void checkStateIsOpen() {
    if (this.state == RoomState.CLOSED) {
      throw new RoomStateException("ルームは既にクローズされています: %s".formatted(this.id));
    }
  }

  private void checkUserIsOwner(User user) {
    if (!this.owner.equals(user)) {
      throw new RoomOperationException("ルームオーナー以外には許可されていません: %s".formatted(this.id));
    }
  }
}
