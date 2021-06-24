package me.shaakashee.imgroup.hashing;

import javafx.scene.image.Image;

import java.util.ArrayList;

public class SimpleDifferenceHashing {

    public static String hashImage(Image img){
        double w = img.getWidth();
        double h = img.getHeight();

        float[] averages = new float[81];
        int[] counts = new int[81];

        float[] averagesPar = new float[40];
        int[] countsPar = new int[40];

        int r = 0, g = 0, b = 0;
        int satLow = 0, satHigh = 0, satMid = 0;
        int bLow = 0, bHigh = 0, bMid = 0;

        for (int i = 0; i < w; i++){
            for (int j = 0; j < h; j++){
                int posI = ((int)((i/w)*9))*9;
                int posJ = ((int)((j/h)*9));
                averages[posI+posJ] += img.getPixelReader().getColor(i, j).grayscale().getRed();
                counts[posI+posJ]++;

                averages[((j*(int)w)+i)%40] += img.getPixelReader().getColor(i, j).grayscale().getRed();
                counts[((j*(int)w)+i)%40]++;

                if (img.getPixelReader().getColor(i, j).getRed() > img.getPixelReader().getColor(i, j).getGreen() && img.getPixelReader().getColor(i, j).getRed() > img.getPixelReader().getColor(i, j).getBlue()){
                    r++;
                }
                if (img.getPixelReader().getColor(i, j).getGreen() > img.getPixelReader().getColor(i, j).getRed() && img.getPixelReader().getColor(i, j).getGreen() > img.getPixelReader().getColor(i, j).getBlue()){
                    g++;
                }
                if (img.getPixelReader().getColor(i, j).getBlue() > img.getPixelReader().getColor(i, j).getGreen() && img.getPixelReader().getColor(i, j).getBlue() > img.getPixelReader().getColor(i, j).getRed()){
                    b++;
                }
                if (img.getPixelReader().getColor(i, j).getSaturation() > 0.66){
                    satHigh++;
                } else if (img.getPixelReader().getColor(i, j).getSaturation() > 0.33){
                    satMid++;
                } else {
                    satLow++;
                }

                if (img.getPixelReader().getColor(i, j).getBrightness() > 0.66){
                    bHigh++;
                } else if (img.getPixelReader().getColor(i, j).getBrightness() > 0.33){
                    bMid++;
                } else {
                    bLow++;
                }
            }
        }

        for (int i = 0; i < averages.length; i++){
            //System.out.println(averages[i]);
            averages[i] /= counts[i];
        }

        StringBuilder ret = new StringBuilder();

        for (int i = 1; i < averages.length; i++){
            ret.append((averages[i]>averages[i-1]?"1":"0"));
        }

        return ret.toString();

    }

}
