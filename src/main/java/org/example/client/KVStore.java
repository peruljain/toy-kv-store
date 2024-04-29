package org.example.client;

import org.example.storage.IStorage;

import java.util.List;
import java.util.Optional;

public class KVStore implements IKVStore{
    private final List<IStorage> storages;

    public KVStore(List<IStorage> storages) {
        this.storages = storages;
    }

    @Override
    public void put(String key, String value, int ttlInSeconds) {
        int hashCode = key.hashCode();
        int index = hashCode%storages.size();
        IStorage storage = storages.get(index);
        storage.put(key, value, ttlInSeconds);
    }

    @Override
    public Optional<String> get(String key) {
        int hashCode = key.hashCode();
        int index = hashCode%storages.size();
        IStorage storage = storages.get(index);
        return storage.get(key);
    }

    @Override
    public void delete(String key) {
        int hashCode = key.hashCode();
        int index = hashCode%storages.size();
        IStorage storage = storages.get(index);
        storage.delete(key);
    }
}
