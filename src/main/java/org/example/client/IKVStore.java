package org.example.client;

import java.util.Optional;

public interface IKVStore {
    void put(String key, String value, int ttlInSeconds);
    Optional<String> get(String key);
    void delete(String key);
}
