package me.shaakashee.imgroup;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import jdk.nashorn.internal.parser.JSONParser;
import me.shaakashee.imgroup.model.Div;
import me.shaakashee.imgroup.model.HashImage;

import java.io.*;
import java.util.HashMap;

public class SaveData {

    public static final String path = "imageSaveData.ighd";

    public static void saveUpdateData(Div div){
        Gson gson = new Gson();

        HashMap<String, smallHashImg> dataMap = getSaved();


        for (HashImage hashImage: div.getImgs()){
            smallHashImg img = new smallHashImg();
            img.hash = hashImage.getHash();
            img.path = hashImage.getFile().getAbsolutePath();
            dataMap.put(hashImage.getFile().getAbsolutePath(),img);
        }

        PrintWriter writer = null;
        try {
            File temp = new File(path);
            writer = new PrintWriter(temp);
            for (smallHashImg img: dataMap.values()) {
                writer.println(gson.toJson(img));
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static HashMap<String, smallHashImg> getSaved(){
        Gson gson = new Gson();

        HashMap<String, smallHashImg> dataMap = new HashMap<>();

        FileInputStream fstream = null;
        try {
            fstream = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return dataMap;
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

        String strLine = "";

//Read File Line By Line
        while (true)   {
            try {
                if (!((strLine = br.readLine()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Print the content on the console
            smallHashImg img = gson.fromJson(strLine, smallHashImg.class);
            dataMap.put(img.path, img);
        }

//Close the input stream
        try {
            fstream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dataMap;
    }


    public static class smallHashImg{
        @SerializedName("hash")
        @Expose
        public String hash;
        @SerializedName("path")
        @Expose
        public String path;
    }

}
