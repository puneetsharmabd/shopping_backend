package com.m8.shopping.util.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AppUtil {
    public static String get_photo_upload_path(String fileName, long product_id) throws IOException{
        Files.createDirectories(Paths.get("src\\main\\resources\\static\\uploads\\"+product_id));
        return new File("src\\main\\resources\\static\\uploads\\"+product_id).getAbsolutePath() + "\\" + fileName;

    }
}
