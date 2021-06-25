package me.shaakashee.imgroup;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class ImageCollector {

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
