package me.shaakashee.imgroup.hashing;

import javafx.scene.image.Image;

import java.util.ArrayList;

public class SimpleDifferenceHashing {

    public static String hashImage(Image img){
        double w = img.getWidth();
        double h = img.getHeight();

        float[] averages = new float[81];
        int[] counts = new int[81];

        for (int i = 0; i < w; i++){
            for (int j = 0; j < h; j++){
                int posI = ((int)(i/w)*9)*9;
                int posJ = ((int)(j/h)*9);
                averages[posI+posJ] += img.getPixelReader().getColor(i, j).grayscale().getRed();
                counts[posI+posJ]++;
            }
        }

        for (int i = 0; i < averages.length; i++){
            averages[i] /= counts[i];
        }

        StringBuilder ret = new StringBuilder();

        for (int i = 1; i < averages.length; i++){
            ret.append((averages[i]>averages[i-1]?"1":"0"));
        }

        return ret.toString();

    }

}
