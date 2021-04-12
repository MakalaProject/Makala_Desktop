package org.example.services;


import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;
import javafx.scene.image.Image;
import org.example.model.Picture;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ImageService {
    public static Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "dwfqqaj64",
            "api_key", "346552885155737",
            "api_secret", "WCVb9nnKC2tpQkCgyrAkoO5CNwY"));


    public static List<String> uploadImages(List<String> images){
        List<String> urls = new ArrayList<>();
        List<String> currentImages = new ArrayList<>();
        for (String f: images){
            if(!f.contains("http://res.cloudinary.com")){
                currentImages.add(f);
            }
        }
        try {
            for(String i: currentImages){
                File image = new File(i);
                Map uploadResult = cloudinary.uploader().upload(image, ObjectUtils.asMap(
                        "folder", "Makala"));
                urls.add((String)uploadResult.get("url"));
            }
            return urls;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void deleteImages(List<String> images){
        try {
            for(String i: images){
                if(i.contains("http://res.cloudinary.com")){
                    String publicId = i.substring(i.length()-24, i.length()-4);
                    Map uploadResult = cloudinary.uploader().destroy("Makala/" + publicId, ObjectUtils.emptyMap());
                    uploadResult.entrySet();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
