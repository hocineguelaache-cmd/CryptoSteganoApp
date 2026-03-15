package fr.securitycrypto.utils;

import java.io.File;

public class FileUtils {
    public static String getExtension(String fileName) {
        int dot = fileName.lastIndexOf('.');
        return dot == -1 ? "" : fileName.substring(dot + 1);
    }
}