package jp.co.sunarch.telework.kokaon.util;

import lombok.experimental.UtilityClass;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author takeshi
 */
@UtilityClass
public class Sets {

  public <T> Set<T> add(Set<T> set, T t) {
    return Stream.concat(set.stream(), Stream.of(t)).collect(Collectors.toSet());
  }

  public <T> Set<T> remove(Set<T> set, T t) {
    return set.stream().filter(s -> !s.equals(t)).collect(Collectors.toSet());
  }
 
}
