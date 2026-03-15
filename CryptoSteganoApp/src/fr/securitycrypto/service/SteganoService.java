package fr.securitycrypto.service;

import fr.securitycrypto.crypto.AESCipher;
import fr.securitycrypto.steganography.LSBEncoder;
import fr.securitycrypto.steganography.LSBDecoder;
import fr.securitycrypto.utils.KeyUtils;
import javax.crypto.SecretKey;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SteganoService {
    public String generateAESKey() {
        try {
            SecretKey key = AESCipher.generateKey();
            return KeyUtils.keyToHex(key);
        } catch (Exception e) {
            throw new RuntimeException("Génération clé échouée", e);
        }
    }

    public File encryptAndHide(String imagePath, String message, String keyHex) throws Exception {
        SecretKey key = KeyUtils.hexToKey(keyHex);
        byte[] encryptedData = AESCipher.encrypt(message.getBytes("UTF-8"), key);
        File outputImage = new File(generateOutputFileName(imagePath));
        LSBEncoder.hideData(imagePath, encryptedData, outputImage.getAbsolutePath());
        return outputImage;
    }

    public String extractAndDecrypt(String stegoImagePath, String keyHex) throws Exception {
        SecretKey key = KeyUtils.hexToKey(keyHex);
        byte[] extracted = LSBDecoder.extractData(stegoImagePath);
        byte[] decrypted = AESCipher.decrypt(extracted, key);
        return new String(decrypted, "UTF-8");
    }

    private String generateOutputFileName(String originalPath) {
        String base = originalPath.substring(0, originalPath.lastIndexOf('.'));
        return base + "_stego.png";
    }
}