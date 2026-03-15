package fr.securitycrypto.steganography;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.ByteBuffer;

public class LSBEncoder {
    public static void hideData(String coverPath, byte[] data, String outputPath) throws Exception {
        BufferedImage img = ImageIO.read(new File(coverPath));
        if (img == null) throw new IllegalArgumentException("Image non valide");

        // Préparer les données : longueur (4 bytes) + données
        byte[] lengthBytes = ByteBuffer.allocate(4).putInt(data.length).array();
        byte[] fullData = new byte[lengthBytes.length + data.length];
        System.arraycopy(lengthBytes, 0, fullData, 0, lengthBytes.length);
        System.arraycopy(data, 0, fullData, lengthBytes.length, data.length);

        int maxBytes = (img.getWidth() * img.getHeight() * 3) / 8;
        if (fullData.length > maxBytes) {
            throw new IllegalArgumentException("Message trop long pour cette image.");
        }

        int dataIndex = 0;
        outer:
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                int rgb = img.getRGB(x, y);
                for (int c = 0; c < 3; c++) { // R, G, B
                    if (dataIndex >= fullData.length * 8) break outer;
                    int bit = (fullData[dataIndex / 8] >> (7 - (dataIndex % 8))) & 1;
                    int component = (rgb >> (16 - c*8)) & 0xFF;
                    component = (component & 0xFE) | bit;
                    rgb = (rgb & ~(0xFF << (16 - c*8))) | (component << (16 - c*8));
                    dataIndex++;
                }
                img.setRGB(x, y, rgb);
            }
        }
        ImageIO.write(img, "png", new File(outputPath));
    }
}