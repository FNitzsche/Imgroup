package me.shaakashee.imgroup.hashing;

import javafx.scene.image.Image;

import java.util.ArrayList;

public class SimpleDifferenceHashing {

    public static String hashImage(Image img){
        double w = img.getWidth();
        double h = img.getHeight();

        float[] averages = new float[81];
        int[] counts = new int[81];

        //float[] averagesPar = new float[41];
        //int[] countsPar = new int[41];

        int r = 0, g = 0, b = 0;
        int[] satLow = new int[9], satHigh =  new int[9], satMid =  new int[9], satMidlow =  new int[9], satMidHigh =  new int[9];
        int[] bLow =  new int[9], bHigh =  new int[9], bMid =  new int[9], bMidlow =  new int[9], bMidhigh =  new int[9];

        for (int i = 0; i < w; i++){
            for (int j = 0; j < h; j++){
                int posI = ((int)((i/w)*9))*9;
                int posJ = ((int)((j/h)*9));
                averages[posI+posJ] += img.getPixelReader().getColor(i, j).grayscale().getRed();
                counts[posI+posJ]++;

                int posI2 = ((int)((i/w)*3))*3;
                int posJ2 = ((int)((j/h)*3));

                //averagesPar[((j*(int)w)+i)%41] += img.getPixelReader().getColor(i, j).grayscale().getRed();
                //countsPar[((j*(int)w)+i)%41]++;

                if (img.getPixelReader().getColor(i, j).getRed() > img.getPixelReader().getColor(i, j).getGreen() && img.getPixelReader().getColor(i, j).getRed() > img.getPixelReader().getColor(i, j).getBlue()){
                    r++;
                }
                if (img.getPixelReader().getColor(i, j).getGreen() > img.getPixelReader().getColor(i, j).getRed() && img.getPixelReader().getColor(i, j).getGreen() > img.getPixelReader().getColor(i, j).getBlue()){
                    g++;
                }
                if (img.getPixelReader().getColor(i, j).getBlue() > img.getPixelReader().getColor(i, j).getGreen() && img.getPixelReader().getColor(i, j).getBlue() > img.getPixelReader().getColor(i, j).getRed()){
                    b++;
                }
                if (img.getPixelReader().getColor(i, j).getSaturation() > 0.8){
                    satHigh[posI2 +posJ2]++;
                } else if (img.getPixelReader().getColor(i, j).getSaturation() > 0.6){
                    satMidHigh[posI2 +posJ2]++;
                } else if (img.getPixelReader().getColor(i, j).getSaturation() > 0.4){
                    satMid[posI2 +posJ2]++;
                } else if (img.getPixelReader().getColor(i, j).getSaturation() > 0.2){
                    satMidlow[posI2 +posJ2]++;
                } else {
                    satLow[posI2 +posJ2]++;
                }

                if (img.getPixelReader().getColor(i, j).getBrightness() > 0.8){
                    bHigh[posI2 +posJ2]++;
                } else if (img.getPixelReader().getColor(i, j).getBrightness() > 0.6){
                    bMidhigh[posI2 +posJ2]++;
                } else if (img.getPixelReader().getColor(i, j).getBrightness() > 0.4){
                    bMid[posI2 +posJ2]++;
                } else if (img.getPixelReader().getColor(i, j).getBrightness() > 0.2){
                    bMidlow[posI2 +posJ2]++;
                } else {
                    bLow[posI2 +posJ2]++;
                }
            }
        }

        for (int i = 0; i < averages.length; i++){
            //System.out.println(averages[i]);
            averages[i] /= counts[i];
        }

        /*for (int i = 0; i < averagesPar.length; i++){
            //System.out.println(averages[i]);
            averagesPar[i] /= countsPar[i];
        }*/

        StringBuilder ret = new StringBuilder();

        for (int i = 1; i < averages.length; i++){
            ret.append((averages[i]>averages[i-1]?"1":"0"));
        }

        ret.append((r > g? "1":"0"));
        ret.append((g > b? "1":"0"));
        ret.append((r+g > b? "1":"0"));
        ret.append((r+b > g? "1":"0"));
        ret.append((g+b > r? "1":"0"));
        for (int i = 0; i < 9; i++) {
            ret.append((satHigh[i] > satMidHigh[i] ? "1" : "0"));
            ret.append((satMidHigh[i] > satMid[i] ? "1" : "0"));
            ret.append((satMid[i] > satMidlow[i] ? "1" : "0"));
            ret.append((satMidlow[i] > satLow[i] ? "1" : "0"));
            ret.append((bHigh[i] > bMidhigh[i] ? "1" : "0"));
            ret.append((bMidhigh[i] > bMid[i] ? "1" : "0"));
            ret.append((bMid[i] > bMidlow[i] ? "1" : "0"));
            ret.append((bMidlow[i] > bLow[i] ? "1" : "0"));
        }
        ret.append("0");

        /*for (int i = 1; i < averagesPar.length; i++){
            ret.append((averagesPar[i]>averagesPar[i-1]?"1":"0"));
        }*/

        return ret.toString();

    }

}
