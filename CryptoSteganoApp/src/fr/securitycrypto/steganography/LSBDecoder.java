package fr.securitycrypto.steganography;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;

public class LSBDecoder {
    public static byte[] extractData(String stegoPath) throws Exception {
        BufferedImage img = ImageIO.read(new File(stegoPath));
        if (img == null) throw new IllegalArgumentException("Image non valide");

        // Lire d'abord les 4 premiers bytes de longueur
        byte[] lengthBytes = extractBits(img, 0, 32);
        int dataLength = ByteBuffer.wrap(lengthBytes).getInt();
        if (dataLength <= 0 || dataLength > img.getWidth() * img.getHeight() * 3 / 8) {
            throw new IllegalArgumentException("Données corrompues ou inexistantes.");
        }
        // Extraire les données réelles
        return extractBits(img, 32, dataLength * 8);
    }

    private static byte[] extractBits(BufferedImage img, int startBit, int bitLength) throws Exception {
        byte[] result = new byte[(bitLength + 7) / 8];
        int bitIdx = 0;
        int pixelIdx = 0;
        outer:
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                int rgb = img.getRGB(x, y);
                for (int c = 0; c < 3; c++) {
                    if (bitIdx >= startBit + bitLength) break outer;
                    if (bitIdx >= startBit) {
                        int component = (rgb >> (16 - c*8)) & 0xFF;
                        int bit = component & 1;
                        int bytePos = (bitIdx - startBit) / 8;
                        int bitPos = 7 - ((bitIdx - startBit) % 8);
                        result[bytePos] |= (bit << bitPos);
                    }
                    bitIdx++;
                }
            }
        }
        return result;
    }
}