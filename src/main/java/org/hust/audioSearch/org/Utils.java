package org;

public class Utils {
    public static String getFileExtension(String fileName){
        String[] fileNameParts = fileName.split("\\.");
        return fileNameParts[1];
    }

    public static String getBaseName(String fileName){
        String[] fileNameParts = fileName.split("\\.");
        return fileNameParts[0];
    }
}
