package fr.securitycrypto.service;

import fr.securitycrypto.crypto.*;

public class ClassicCryptoService {
    public String encrypt(String algorithm, String text, String key) {
        switch (algorithm) {
            case "César":
                int shift = Integer.parseInt(key);
                return CaesarCipher.encrypt(text, shift);
            case "Playfair":
                return PlayfairCipher.encrypt(text, key);
            case "Vigenère":
                return VigenereCipher.encrypt(text, key);
            default:
                throw new IllegalArgumentException("Algorithme inconnu");
        }
    }

    public String decrypt(String algorithm, String text, String key) {
        switch (algorithm) {
            case "César":
                int shift = Integer.parseInt(key);
                return CaesarCipher.decrypt(text, shift);
            case "Playfair":
                return PlayfairCipher.decrypt(text, key);
            case "Vigenère":
                return VigenereCipher.decrypt(text, key);
            default:
                throw new IllegalArgumentException("Algorithme inconnu");
        }
    }
}