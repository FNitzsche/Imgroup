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
        int satLow = 0, satHigh = 0, satMid = 0, satMidlow = 0, satMidHigh = 0;
        int bLow = 0, bHigh = 0, bMid = 0, bMidlow = 0, bMidhigh = 0;

        for (int i = 0; i < w; i++){
            for (int j = 0; j < h; j++){
                int posI = ((int)((i/w)*9))*9;
                int posJ = ((int)((j/h)*9));
                averages[posI+posJ] += img.getPixelReader().getColor(i, j).grayscale().getRed();
                counts[posI+posJ]++;

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
                    satHigh++;
                } else if (img.getPixelReader().getColor(i, j).getSaturation() > 0.6){
                    satMidHigh++;
                } else if (img.getPixelReader().getColor(i, j).getSaturation() > 0.4){
                    satMid++;
                } else if (img.getPixelReader().getColor(i, j).getSaturation() > 0.2){
                    satMidlow++;
                } else {
                    satLow++;
                }

                if (img.getPixelReader().getColor(i, j).getBrightness() > 0.8){
                    bHigh++;
                } else if (img.getPixelReader().getColor(i, j).getBrightness() > 0.6){
                    bMidhigh++;
                } else if (img.getPixelReader().getColor(i, j).getBrightness() > 0.4){
                    bMid++;
                } else if (img.getPixelReader().getColor(i, j).getBrightness() > 0.2){
                    bMidlow++;
                } else {
                    bLow++;
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
        ret.append((satHigh > satMidHigh? "1":"0"));
        ret.append((satMidHigh > satMid? "1":"0"));
        ret.append((satMid > satMidlow? "1":"0"));
        ret.append((satMidlow > satLow? "1":"0"));
        ret.append((bHigh > bMidhigh? "1":"0"));
        ret.append((bMidhigh > bMid? "1":"0"));
        ret.append((bMid > bMidlow? "1":"0"));
        ret.append((bMidlow > bLow? "1":"0"));
        ret.append("0");

        /*for (int i = 1; i < averagesPar.length; i++){
            ret.append((averagesPar[i]>averagesPar[i-1]?"1":"0"));
        }*/

        return ret.toString();

    }

}
