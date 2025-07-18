package com.septeo.ulyses.technical.test.cache;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class SimpleCache<K, V> {

    private static class CacheEntry<V> {
        V value;

        CacheEntry(V value) {
            this.value = value;
        }
    }

    private final Map<K, CacheEntry<V>> store = new ConcurrentHashMap<>();
    private final long ttlMillis;
    private Instant expirationTime;
    private boolean fullyLoaded = false;

    public SimpleCache(long ttlMillis) {
        this.ttlMillis = ttlMillis;
        this.expirationTime = Instant.now().plusMillis(ttlMillis);
    }

    private boolean isExpired() {
        return Instant.now().isAfter(expirationTime);
    }

    private void resetExpiration() {
        this.expirationTime = Instant.now().plusMillis(ttlMillis);
    }

    private void invalidateIfExpired() {
        if (isExpired()) {
            store.clear();
            fullyLoaded = false;
        }
    }

    public V getOneOrLoad(K key, Supplier<V> supplier) {
        invalidateIfExpired();
        CacheEntry<V> entry = store.get(key);
        if (entry != null) {
            resetExpiration();
            return entry.value;
        }

        V value = supplier.get();
        if (value != null) {
            store.put(key, new CacheEntry<>(value));
            resetExpiration();
        }
        return value;
    }

    public Map<K, V> getAllOrLoad(Supplier<Map<K, V>> supplier) {
        invalidateIfExpired();
        if (fullyLoaded && !store.isEmpty()) {
            resetExpiration();
            return store.entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            e -> e.getValue().value
                    ));
        }

        Map<K, V> loadedMap = supplier.get();
        if (loadedMap != null) {
            store.clear();
            loadedMap.forEach((key, value) -> store.put(key, new CacheEntry<>(value)));
            fullyLoaded = true;
            resetExpiration();
        }

        return loadedMap != null ? loadedMap : Map.of();
    }

    public void put(K key, V value) {
        invalidateIfExpired();
        store.put(key, new CacheEntry<>(value));
        resetExpiration();
    }

    public void remove(K key) {
        invalidateIfExpired();
        store.remove(key);
        if (store.isEmpty()) {
            fullyLoaded = false;
        }
    }

}
