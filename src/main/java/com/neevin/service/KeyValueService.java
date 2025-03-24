package com.neevin.service;

import com.neevin.exception.KeyNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class KeyValueService {

    // Класс-обертка для байтовых массивов с корректной реализацией equals и hashCode
    private static class ByteArrayKey {
        private final byte[] bytes;

        public ByteArrayKey(byte[] bytes) {
            this.bytes = bytes;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            ByteArrayKey other = (ByteArrayKey) obj;
            return Arrays.equals(bytes, other.bytes);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(bytes);
        }
    }

    // Используем обертку ByteArrayKey для корректного сравнения ключей
    private final Map<ByteArrayKey, byte[]> storage = new ConcurrentHashMap<>();

    public byte[] get(byte[] key) {
        byte[] value = storage.get(new ByteArrayKey(key));
        if (value == null) {
            throw new KeyNotFoundException("Key not found");
        }
        return value;
    }

    public void put(byte[] key, byte[] value) {
        storage.put(new ByteArrayKey(key), value);
    }

    public void delete(byte[] key) {
        if (storage.remove(new ByteArrayKey(key)) == null) {
            throw new KeyNotFoundException("Key not found");
        }
    }

    public boolean exists(byte[] key) {
        return storage.containsKey(new ByteArrayKey(key));
    }
}

