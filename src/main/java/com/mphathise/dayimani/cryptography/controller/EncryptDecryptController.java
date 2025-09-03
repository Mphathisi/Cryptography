package com.mphathise.dayimani.cryptography.controller;
import com.mphathise.dayimani.cryptography.service.EncryptDecryptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class EncryptDecryptController {

    private final EncryptDecryptService encryptDecryptService;

    @PostMapping("/encrypt")
    public ResponseEntity<Map<String, String>> encrypt(@RequestBody Map<String, String> request) {
        try {
            String plainText = request.get("text");
            if (plainText == null || plainText.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Text to encrypt cannot be empty"));
            }

            String encrypted = encryptDecryptService.encrypt(plainText);
            log.info("Successfully encrypted text of length: {}", plainText.length());

            return ResponseEntity.ok(Map.of(
                    "encrypted", encrypted,
                    "status", "success"
            ));
        } catch (Exception e) {
            log.error("Encryption failed", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Encryption failed: " + e.getMessage()));
        }
    }

    @PostMapping("/decrypt")
    public ResponseEntity<Map<String, String>> decrypt(@RequestBody Map<String, String> request) {
        try {
            String encryptedText = request.get("encryptedText");
            if (encryptedText == null || encryptedText.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Encrypted text cannot be empty"));
            }

            String decrypted = encryptDecryptService.decrypt(encryptedText);
            log.info("Successfully decrypted text");

            return ResponseEntity.ok(Map.of(
                    "decrypted", decrypted,
                    "status", "success"
            ));
        } catch (Exception e) {
            log.error("Decryption failed", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Decryption failed: " + e.getMessage()));
        }
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "Encrypt/Decrypt Service",
                "timestamp", String.valueOf(System.currentTimeMillis())
        ));
    }
}
