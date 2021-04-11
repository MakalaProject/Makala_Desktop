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

    public static List<String> sendImageList(List<File> images){
        List<String> urls = new ArrayList<>();
        try {
            for(File image: images){
                Map uploadResult = cloudinary.uploader().upload(image, ObjectUtils.asMap(
                        "folder", "Makala",
                                "public_id"));
                urls.add((String)uploadResult.get("url"));
            }
            return urls;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> updateImageList(List<File> images, List<Picture> pictures){

        List<String> urls = new ArrayList<>();
        try {
            for(File image: images){
                Map uploadResult = cloudinary.uploader().upload(image, ObjectUtils.asMap(
                        "folder", "Makala",
                        "public_id"));
                urls.add((String)uploadResult.get("url"));
            }
            return urls;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
