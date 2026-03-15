package fr.securitycrypto.crypto;

public class VigenereCipher {
    public static String encrypt(String text, String key) {
        key = key.toUpperCase().replaceAll("[^A-Z]", "");
        StringBuilder result = new StringBuilder();
        int keyIndex = 0;
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                int shift = key.charAt(keyIndex % key.length()) - 'A';
                c = (char) ((c - base + shift) % 26 + base);
                keyIndex++;
            }
            result.append(c);
        }
        return result.toString();
    }

    public static String decrypt(String text, String key) {
        key = key.toUpperCase().replaceAll("[^A-Z]", "");
        StringBuilder result = new StringBuilder();
        int keyIndex = 0;
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                int shift = key.charAt(keyIndex % key.length()) - 'A';
                c = (char) ((c - base - shift + 26) % 26 + base);
                keyIndex++;
            }
            result.append(c);
        }
        return result.toString();
    }
}