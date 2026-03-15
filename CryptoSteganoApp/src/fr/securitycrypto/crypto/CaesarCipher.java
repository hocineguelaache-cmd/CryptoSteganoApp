package fr.securitycrypto.crypto;

public class CaesarCipher {
    public static String encrypt(String text, int shift) {
        shift = shift % 26;
        StringBuilder sb = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                c = (char) ((c - base + shift) % 26 + base);
            }
            sb.append(c);
        }
        return sb.toString();
    }

    public static String decrypt(String text, int shift) {
        return encrypt(text, 26 - (shift % 26));
    }
}