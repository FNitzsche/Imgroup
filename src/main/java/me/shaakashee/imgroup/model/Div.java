package me.shaakashee.imgroup.model;

import java.io.File;
import java.util.ArrayList;

public class Div {

    private File path;

    private ArrayList<HashImage> imgs;

    public File getPath() {
        return path;
    }

    public void setPath(File path) {
        this.path = path;
    }

    public ArrayList<HashImage> getImgs() {
        return imgs;
    }

    public void setImgs(ArrayList<HashImage> imgs) {
        this.imgs = imgs;
    }
}
