package org.example.storage;

import java.util.Optional;

public interface IStorage {
    void put(String key, String value, int ttlInSeconds);
    Optional<String> get(String key);

    void  delete(String key);
}
