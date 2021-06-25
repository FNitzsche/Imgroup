package me.shaakashee.imgroup;

import me.shaakashee.imgroup.model.Div;
import me.shaakashee.imgroup.model.HashImage;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.stream.Collectors;

public class ImageCollector {

    public static void setImages(Div div){
        div.setImgs(collectImages(div.getPath()).stream().map(file -> {
            HashImage hashImage = new HashImage();
            hashImage.setFile(file);
            return hashImage;
        }).collect(Collectors.toCollection(ArrayList::new)));
    }

    public static void loadPrevs(Div div){
        div.getImgs().parallelStream().forEach(img -> HashImage.getPrev(img));
    }

    public static void loadHashes(Div div){
        div.getImgs().parallelStream().forEach(img -> {
            if (img.getHash() == null){
                HashImage.calcHash(img);
            }
        });
    }

    public static ArrayList<HashImage> getDistTo(HashImage hashImage, Div div, double c, double c2, double c3){
        div.getImgs().parallelStream().forEach(img -> img.setCurrentDistance(HashImage.getDistance(hashImage,img, c, c2, c3)));
        return div.getImgs().parallelStream().sorted(Comparator.comparingDouble(HashImage::getCurrentDistance)).collect(Collectors.toCollection(ArrayList::new));
    }

    public static HashSet<File> collectImages(File dir){
        HashSet<File> imgs = new HashSet<>();
        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File f, String name) {
                return name.endsWith(".png");
            }
        };

        FilenameFilter filter2 = new FilenameFilter() {
            @Override
            public boolean accept(File f, String name) {
                return name.endsWith(".jpg");
            }
        };

        FilenameFilter filter3 = new FilenameFilter() {
            @Override
            public boolean accept(File f, String name) {
                return name.endsWith(".JPG");
            }
        };

        FilenameFilter filter4 = new FilenameFilter() {
            @Override
            public boolean accept(File f, String name) {
                return name.endsWith(".JEPG");
            }
        };


        imgs.addAll(Arrays.asList(dir.listFiles(filter)));
        imgs.addAll(Arrays.asList(dir.listFiles(filter2)));
        imgs.addAll(Arrays.asList(dir.listFiles(filter3)));
        imgs.addAll(Arrays.asList(dir.listFiles(filter4)));

        for (File file :dir.listFiles()){
            if (file.isDirectory()){
                imgs.addAll(collectImages(file));
            }
        }

        return imgs;
    }

}
