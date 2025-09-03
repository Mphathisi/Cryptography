package com.mphathise.dayimani.cryptography.service.impl;

import com.mphathise.dayimani.cryptography.service.EncryptDecryptService;

import com.macasaet.fernet.Key;
import com.macasaet.fernet.Token;
import com.macasaet.fernet.Validator;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.function.Function;


@Service
public class EncryptDecryptServiceImpl implements EncryptDecryptService {

    private final Key key;

    public EncryptDecryptServiceImpl() {
        this.key = new Key("CjSUtCy9pw5wX6gbb9cdY1Cz_4iNe1tQ7VV43ImMYu4=");
    }

    @Override
    public String encrypt(String plainText) {
        try {
            Token token = Token.generate(key, plainText);
            return "ENC[" + token.serialise() + "]";
        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }

    @Override
    public String decrypt(String encryptedText) {
        try {
            int start = encryptedText.indexOf("ENC[") + "ENC[".length();
            int end = encryptedText.indexOf("]", start);
            String tokenString = encryptedText.substring(start, end);

            Token token = Token.fromString(tokenString);

            return token.validateAndDecrypt(key, new Validator<>() {
                @Override
                public Duration getTimeToLive() {
                    return Duration.ofDays(61210);
                }

                @Override
                public Function<byte[], String> getTransformer() {
                    return String::new;
                }
            });
        } catch (Exception e) {
            throw new RuntimeException("Decryption failed", e);
        }
    }
}

