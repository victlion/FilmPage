package com.example.filmboock.base;

import java.io.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ImportBase {
    private String zipFilePath = "DATA/archive.zip";
    private String[] destDirectory = {"DATA","DATA/Images"};
    public ImportBase(File path) {
        zipFilePath = String.valueOf(path);
        try {
            unzip(zipFilePath);
            System.out.println("Файлы успешно извлечены в: " + destDirectory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void unzip(String zipFilePath) throws IOException {
        try (ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry entry = zipIn.getNextEntry();

            while (entry != null) {
                String filePath;
                String[] type = (entry.getName()).split("\\.");
                if(type[type.length-1].trim().equalsIgnoreCase("json")){
                    filePath = destDirectory[0] + File.separator + entry.getName();
                }else {
                    filePath = destDirectory[1] + File.separator + entry.getName();
                }
                if (!entry.isDirectory()) {
                    extractFile(zipIn, filePath);
                } else {
                    createDirectory(filePath);
                }

                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
        }
    }

    private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath))) {
            byte[] bytes = new byte[1024];
            int length;

            while ((length = zipIn.read(bytes)) > 0) {
                bos.write(bytes, 0, length);
            }
        }
    }

    private static void createDirectory(String filePath) {
        File dir = new File(filePath);
        dir.mkdirs();
    }
}
