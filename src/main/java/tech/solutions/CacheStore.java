package tech.solutions;

import java.util.Optional;

public interface CacheStore<V> {

    void setTTL(int duration);

    void add(String key1, V value);

    Optional<V> get(String key);
}
