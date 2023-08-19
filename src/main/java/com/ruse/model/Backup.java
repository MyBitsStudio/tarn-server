package com.ruse.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Backup {

    public static List<String> filesList= new ArrayList<>();

    public static void backup(){
        String BACKUP_DIR_NEW = "./.core/security/backup/";
        File dir = new File(BACKUP_DIR_NEW);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        filesList.clear();

        try (FileOutputStream fos = new FileOutputStream(BACKUP_DIR_NEW + date(System.currentTimeMillis()) + "-new.zip");
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            String NEW_SAVES = "./.core/server/security/saves/";
            populateFilesList(new File(NEW_SAVES));


            for (String filePath : filesList) {
                ZipEntry ze = new ZipEntry(filePath.substring(dir.getAbsolutePath().length() - 1)
                        .replace('_', ' '));
                zos.putNextEntry(ze);

                try (FileInputStream fis = new FileInputStream(filePath.replace('_', ' '))) {
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = fis.read(buffer)) > 0) {
                        zos.write(buffer, 0, len);
                    }
                }
                zos.closeEntry();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            Thread.sleep(1000 * 60 * 30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void populateFilesList(File dir) {
        File[] files = dir.listFiles();
        assert files != null;
        for(File file : Objects.requireNonNull(files)){
            if(file == null) continue;
            if(file.isFile()) filesList.add(file.getAbsolutePath().replace(" ", "_"));
            else populateFilesList(file);
        }
    }

    private static String date(long milli){
        return new java.text.SimpleDateFormat("MM/dd/yyyy HH mm").format(new java.util.Date (milli)).replace(" ", "_").replace("/", "-");
    }
}
