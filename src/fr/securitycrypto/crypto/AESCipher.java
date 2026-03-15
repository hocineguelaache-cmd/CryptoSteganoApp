package fr.securitycrypto.crypto;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import java.security.SecureRandom;

public class AESCipher {
    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH = 128;
    private static final int GCM_IV_LENGTH = 12;

    public static SecretKey generateKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        return keyGen.generateKey();
    }

    public static byte[] encrypt(byte[] plaintext, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        byte[] iv = new byte[GCM_IV_LENGTH];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, spec);
        byte[] ciphertext = cipher.doFinal(plaintext);
        // Préfixer l'IV
        byte[] result = new byte[iv.length + ciphertext.length];
        System.arraycopy(iv, 0, result, 0, iv.length);
        System.arraycopy(ciphertext, 0, result, iv.length, ciphertext.length);
        return result;
    }

    public static byte[] decrypt(byte[] ciphertextWithIv, SecretKey key) throws Exception {
        byte[] iv = new byte[GCM_IV_LENGTH];
        byte[] ciphertext = new byte[ciphertextWithIv.length - GCM_IV_LENGTH];
        System.arraycopy(ciphertextWithIv, 0, iv, 0, GCM_IV_LENGTH);
        System.arraycopy(ciphertextWithIv, GCM_IV_LENGTH, ciphertext, 0, ciphertext.length);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, spec);
        return cipher.doFinal(ciphertext);
    }
}