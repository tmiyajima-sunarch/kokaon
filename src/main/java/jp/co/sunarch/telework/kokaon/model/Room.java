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
  UserId ownerId;
  @With(AccessLevel.PRIVATE)
  RoomState state;
  @With(AccessLevel.PRIVATE)
  Set<UserId> memberIds;
  @With(AccessLevel.PRIVATE)
  Set<AudioId> audioIds;

  public static Room of(RoomId id, String name, User owner) {
    return new Room(id, name, owner.getId(), RoomState.OPEN, Set.of(), Set.of());
  }

  public Room enter(User user) {
    this.checkStateIsOpen();
    return this.withMemberIds(Sets.add(this.memberIds, user.getId()));
  }

  public Room leave(User user) {
    this.checkStateIsOpen();
    return this.withMemberIds(Sets.remove(this.memberIds, user.getId()));
  }

  public Room addAudioBy(User user, Audio audio) {
    this.checkStateIsOpen();
    this.checkUserIsMember(user);
    return this.withAudioIds(Sets.add(this.audioIds, audio.getId()));
  }

  public Room removeAudioBy(User user, Audio audio) {
    this.checkStateIsOpen();
    this.checkUserIsMember(user);
    return this.withAudioIds(Sets.remove(this.audioIds, audio.getId()));
  }

  public Room closeBy(User user) {
    this.checkStateIsOpen();
    this.checkUserIsOwner(user);
    return this.withState(RoomState.CLOSED);
  }

  public boolean isMember(User user) {
    return this.memberIds.contains(user.getId());
  }

  private void checkStateIsOpen() {
    if (this.state == RoomState.CLOSED) {
      throw new RoomStateException("ルームは既にクローズされています: %s".formatted(this.id.value()));
    }
  }

  private void checkUserIsMember(User user) {
    if (!this.ownerId.equals(user.getId()) && !this.memberIds.contains(user.getId())) {
      throw new RoomOperationException("ルームメンバー以外には許可されていません: %s".formatted(this.id.value()));
    }
  }

  private void checkUserIsOwner(User user) {
    if (!this.ownerId.equals(user.getId())) {
      throw new RoomOperationException("ルームオーナー以外には許可されていません: %s".formatted(this.id.value()));
    }
  }

}
