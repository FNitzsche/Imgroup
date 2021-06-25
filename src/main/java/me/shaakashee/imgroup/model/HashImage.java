package me.shaakashee.imgroup.model;

import javafx.scene.image.Image;
import me.shaakashee.imgroup.hashing.SimpleDifferenceHashing;

import java.io.File;

public class HashImage {

    private File file;
    private String hash;
    private Image prev;

    private double currentDistance;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Image getPrev() {
        return prev;
    }

    public void setPrev(Image prev) {
        this.prev = prev;
    }

    public static void getPrev(HashImage hashImage){
        Image img = new Image("file:" + hashImage.file.getAbsolutePath(), 100, 100, true, true);
        hashImage.setPrev(img);
    }

    public static void calcHash(HashImage hashImage){
        Image img = new Image("file:" + hashImage.file.getAbsolutePath(), 500, 500, true, true);
        hashImage.setHash(SimpleDifferenceHashing.hashImage(img));
    }

    public static double getDistance(HashImage hashImage, HashImage hashImage2, double c, double c2, double c3){
        if (hashImage.getHash() == null || hashImage2.getHash() == null){
            return -1;
        }

        double diffCount = 0;

        for (int i = 0; i < 80; i++){
            if (hashImage.getHash().charAt(i) != hashImage2.getHash().charAt(i)){
                diffCount+=2*c;
            }
        }

        for (int i = 80; i < 94; i++){
            if (hashImage.getHash().charAt(i) != hashImage2.getHash().charAt(i)){
                diffCount+=20*c2;
            }
        }

        /*for (int i = 94; i < hashImage.getHash().length(); i++){
            if (hashImage.getHash().charAt(i) != hashImage2.getHash().charAt(i)){
                diffCount+=c3;
            }
        }*/

        return diffCount;
    }

    public double getCurrentDistance() {
        return currentDistance;
    }

    public void setCurrentDistance(double currentDistance) {
        this.currentDistance = currentDistance;
    }
}
