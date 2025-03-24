package com.neevin.controller;

import com.neevin.exception.KeyNotFoundException;
import com.neevin.model.KeyValueRequest;
import com.neevin.service.KeyValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/kv")
public class KeyValueController {

    private final KeyValueService keyValueService;

    @Autowired
    public KeyValueController(KeyValueService keyValueService) {
        this.keyValueService = keyValueService;
    }

    @PostMapping("/get")
    public ResponseEntity<Map<String, String>> getValue(@RequestBody KeyValueRequest request) {
        // Декодируем Base64-ключ в байты
        byte[] keyBytes = Base64.getDecoder().decode(request.getKey());

        // Получаем значение из хранилища
        byte[] valueBytes = keyValueService.get(keyBytes);

        // Кодируем байты обратно в Base64 для ответа
        Map<String, String> response = new HashMap<>();
        response.put("value", Base64.getEncoder().encodeToString(valueBytes));

        return ResponseEntity.ok(response);
    }

    @PostMapping("/put")
    public ResponseEntity<Void> putValue(@RequestBody KeyValueRequest request) {
        // Проверка наличия данных
        if (request.getKey() == null || request.getValue() == null) {
            return ResponseEntity.badRequest().build();
        }

        // Декодируем Base64 в байты
        byte[] keyBytes = Base64.getDecoder().decode(request.getKey());
        byte[] valueBytes = Base64.getDecoder().decode(request.getValue());

        // Сохраняем в хранилище
        keyValueService.put(keyBytes, valueBytes);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> deleteValue(@RequestBody KeyValueRequest request) {
        // Декодируем Base64-ключ в байты
        byte[] keyBytes = Base64.getDecoder().decode(request.getKey());

        // Удаляем из хранилища
        keyValueService.delete(keyBytes);

        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(KeyNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleKeyNotFound(KeyNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleInvalidBase64(IllegalArgumentException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Invalid Base64 encoding: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}

