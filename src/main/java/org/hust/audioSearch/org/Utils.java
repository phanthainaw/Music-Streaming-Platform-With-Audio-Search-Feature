package org.hust.audioSearch.org;

public class Utils {
    public static String getFileExtension(String fileName){
        String[] fileNameParts = fileName.split("\\.");
        return fileNameParts[1];
    }

    public static String getBaseName(String fileName){
        String[] fileNameParts = fileName.split("\\.");
        return fileNameParts[0];
    }

    public static double standardDeviation(double[] data){
        double sum = 0.0;
        for (int i = 0; i < data.length; i++) {
            sum+= data[i];
        }
        double mean = sum/data.length;
        double sumdifferences=0;
        for (int i = 0; i < data.length; i++) {
            sumdifferences+=(Math.pow(data[i]-mean, 2)/data.length);

        }
        return Math.sqrt(sumdifferences);
    }

    public static double mean(double[] data){
        double sum = 0.0;
        for (int i = 0; i < data.length; i++) {
            sum+= data[i];
        }
        double mean = sum/data.length;
        double sumdifferences=0;
        for (int i = 0; i < data.length; i++) {
            sumdifferences+=(Math.pow(data[i]-mean, 2)/data.length);

        }
        return Math.sqrt(sumdifferences);
    }


}
