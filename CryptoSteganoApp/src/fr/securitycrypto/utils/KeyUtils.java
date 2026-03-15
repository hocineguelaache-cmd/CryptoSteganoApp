package fr.securitycrypto.utils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.HexFormat;

public class KeyUtils {
    public static String keyToHex(SecretKey key) {
        return HexFormat.of().formatHex(key.getEncoded());
    }

    public static SecretKey hexToKey(String hex) {
        byte[] decoded = HexFormat.of().parseHex(hex);
        return new SecretKeySpec(decoded, "AES");
    }
}