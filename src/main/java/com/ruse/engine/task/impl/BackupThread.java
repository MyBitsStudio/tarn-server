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
    private static String SAVE_DIR = "./data/saves/characters/";
    private static String SHOP_DIR = "./data/saves/pos/";

    public List<String> filesList= new ArrayList<>();

    public BackupThread(){
        super(7200, true);
    }
    @Override
    protected void execute() {
        GameEngine.submit(this::backup);
    }

    private void backup(){
        File dir = new File(BACKUP_DIR);
        if(!dir.exists()) dir.mkdirs();

        try{
            populateFilesList(new File(SAVE_DIR));
            populateFilesList(new File(SHOP_DIR));
            FileOutputStream fos = new FileOutputStream(BACKUP_DIR + date(System.currentTimeMillis()) + ".zip");
            ZipOutputStream zos = new ZipOutputStream(fos);

            for(String filePath : filesList){
                ZipEntry ze = new ZipEntry(filePath.substring(dir.getAbsolutePath().length()-1));
                zos.putNextEntry(ze);
                FileInputStream fis = new FileInputStream(filePath);
                byte[] buffer = new byte[1024];
                int len;
                while ((len = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }
                zos.closeEntry();
                fis.close();
            }
            zos.close();
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void populateFilesList(File dir) {
        File[] files = dir.listFiles();
        for(File file : Objects.requireNonNull(files)){
            if(file.isFile()) filesList.add(file.getAbsolutePath());
            else populateFilesList(file);
        }
    }

    private String date(long milli){
        return new java.text.SimpleDateFormat("MM/dd/yyyy HH").format(new java.util.Date (milli)).replace(" ", "_").replace("/", "-");
    }
}
