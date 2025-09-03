package com.mphathise.dayimani.cryptography.service;

public interface EncryptDecryptService {
    String encrypt(String plainText);
    String decrypt(String encryptedText);
}
