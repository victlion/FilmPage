package com.example.filmboock.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ExportBase {
    private final String[] EXPORT_FILES = {"DATA","DATA/Images"};
    private String EXPORT_PATH = "\\archive.zip";
    public ExportBase(String path){
        EXPORT_PATH = path + EXPORT_PATH;
        List<File> listFileExport = new ArrayList<>();

        for (String directoryPath : EXPORT_FILES) {
            File dir = new File(directoryPath);
            File[] arrFiles = dir.listFiles();
            List<File> lst = Arrays.asList(arrFiles);
            listFileExport = Stream.concat(listFileExport.stream(),lst.stream())
                    .collect(Collectors.toList());
        }
        createZip(listFileExport.stream().filter(File::isFile).collect(Collectors.toList()));
    }
    private void createZip(List<File> fileList){
        List<File> filesToZip = fileList;

        try {
            FileOutputStream fos = new FileOutputStream(EXPORT_PATH);
            ZipOutputStream zipOut = new ZipOutputStream(fos);

            for (File file : filesToZip) {
                addToZip(file, zipOut);
            }

            zipOut.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void addToZip(File file, ZipOutputStream zipOut) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        ZipEntry zipEntry = new ZipEntry(file.getName());
        zipOut.putNextEntry(zipEntry);

        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }

        fis.close();
    }
}
