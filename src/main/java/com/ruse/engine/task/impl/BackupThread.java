package com.ruse.engine.task.impl;

import com.ruse.engine.GameEngine;
import com.ruse.engine.task.Task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class BackupThread extends Task {

    private static String BACKUP_DIR = "./data/backups/";

    private static String BACKUP_DIR_NEW = "./data/security/backup/";
    private static String SAVE_DIR = "./data/saves/characters/";

    private static String NEW_SAVES = "./data/security/saves/";

    public List<String> filesList= new ArrayList<>();

    public BackupThread(){
        super(7200, false);
    }
    @Override
    protected void execute() {
        GameEngine.submit(this::backup);
    }

    private void backup() {
        File dir = new File(BACKUP_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try (FileOutputStream fos = new FileOutputStream(BACKUP_DIR + date(System.currentTimeMillis()) + "-char.zip");
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            populateFilesList(new File(SAVE_DIR));


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

        filesList.clear();

        try (FileOutputStream fos = new FileOutputStream(BACKUP_DIR_NEW + date(System.currentTimeMillis()) + "-new.zip");
             ZipOutputStream zos = new ZipOutputStream(fos)) {
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

    }

    private void populateFilesList(File dir) {
        File[] files = dir.listFiles();
        for(File file : Objects.requireNonNull(files)){
            if(file.isFile()) filesList.add(file.getAbsolutePath().replace(" ", "_"));
            else populateFilesList(file);
        }
    }

    private String date(long milli){
        return new java.text.SimpleDateFormat("MM/dd/yyyy HH mm").format(new java.util.Date (milli)).replace(" ", "_").replace("/", "-");
    }
}
