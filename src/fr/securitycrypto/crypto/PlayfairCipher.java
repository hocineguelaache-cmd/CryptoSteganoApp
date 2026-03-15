package fr.securitycrypto.crypto;

import java.util.*;

public class PlayfairCipher {
    private static char[][] matrix = new char[5][5];

    public static String encrypt(String text, String key) {
        prepareMatrix(key);
        text = prepareText(text);
        return process(text, true);
    }

    public static String decrypt(String text, String key) {
        prepareMatrix(key);
        text = prepareText(text);
        return process(text, false);
    }

    private static void prepareMatrix(String key) {
        Set<Character> seen = new LinkedHashSet<>();
        key = key.toUpperCase().replaceAll("[^A-Z]", "").replace('J', 'I');
        for (char c : key.toCharArray()) seen.add(c);
        for (char c = 'A'; c <= 'Z'; c++) if (c != 'J') seen.add(c);
        Iterator<Character> it = seen.iterator();
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++)
                matrix[i][j] = it.next();
    }

    private static String prepareText(String text) {
        text = text.toUpperCase().replaceAll("[^A-Z]", "").replace('J', 'I');
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            sb.append(text.charAt(i));
            if (i + 1 < text.length() && text.charAt(i) == text.charAt(i + 1))
                sb.append('X');
        }
        if (sb.length() % 2 != 0) sb.append('X');
        return sb.toString();
    }

    private static String process(String text, boolean encrypt) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length(); i += 2) {
            char a = text.charAt(i);
            char b = text.charAt(i + 1);
            int[] posA = findPos(a);
            int[] posB = findPos(b);
            if (posA[0] == posB[0]) {
                int colA = (posA[1] + (encrypt ? 1 : 4)) % 5;
                int colB = (posB[1] + (encrypt ? 1 : 4)) % 5;
                result.append(matrix[posA[0]][colA]).append(matrix[posB[0]][colB]);
            } else if (posA[1] == posB[1]) {
                int rowA = (posA[0] + (encrypt ? 1 : 4)) % 5;
                int rowB = (posB[0] + (encrypt ? 1 : 4)) % 5;
                result.append(matrix[rowA][posA[1]]).append(matrix[rowB][posB[1]]);
            } else {
                result.append(matrix[posA[0]][posB[1]]).append(matrix[posB[0]][posA[1]]);
            }
        }
        return result.toString();
    }

    private static int[] findPos(char c) {
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++)
                if (matrix[i][j] == c) return new int[]{i, j};
        return null;
    }
}