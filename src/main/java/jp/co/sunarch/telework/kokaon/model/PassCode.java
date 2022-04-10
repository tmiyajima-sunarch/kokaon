package jp.co.sunarch.telework.kokaon.model;

import org.springframework.util.Assert;

import java.security.SecureRandom;
import java.util.regex.Pattern;

/**
 * @author takeshi
 */
public record PassCode(
    String value
) {
  private static final int LENGTH = 6;
  private static final Pattern PATTERN = Pattern.compile("[0-9]+");
  private static final SecureRandom random = new SecureRandom();

  public static PassCode of(String value) {
    Assert.isTrue(value.length() == LENGTH, () -> "The passcode '%s' must have length %d".formatted(value, LENGTH));
    Assert.isTrue(PATTERN.matcher(value).matches(), () -> "The passcode '%s' must match pattern '%s'".formatted(value, PATTERN.pattern()));
    return new PassCode(value);
  }

  public static PassCode randomPassCode() {
    StringBuilder b = new StringBuilder(LENGTH);
    for (int i = 0; i < LENGTH; i++) {
      b.append(random.nextInt(10));
    }
    return PassCode.of(b.toString());
  }

  public boolean matches(String passCode) {
    return this.value.equals(passCode);
  }
}
