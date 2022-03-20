package jp.co.sunarch.telework.kokaon.model;

import lombok.EqualsAndHashCode;

/**
 * ユーザー。
 *
 * @author takeshi
 */
@EqualsAndHashCode(of = "id", callSuper = false)
public record User(
    UserId id,
    String nickname
) {
}
