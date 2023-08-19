//package com.ruse.engine.task.impl;
//
//import com.ruse.engine.GameEngine;
//import com.ruse.engine.task.Task;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//import java.util.zip.ZipEntry;
//import java.util.zip.ZipOutputStream;
//
//public class BackupThread extends Task {
//
//
//
//    public BackupThread(){
//        super(7200, false);
//    }
//    @Override
//    protected void execute() {
//        GameEngine.submit(this::backup);
//    }
//
//    private void backup() {
//
//
//    }
//
//    private void populateFilesList(File dir) {
//        File[] files = dir.listFiles();
//        assert files != null;
//        for(File file : Objects.requireNonNull(files)){
//            if(file == null) continue;
//            if(file.isFile()) filesList.add(file.getAbsolutePath().replace(" ", "_"));
//            else populateFilesList(file);
//        }
//    }
//
//    private String date(long milli){
//        return new java.text.SimpleDateFormat("MM/dd/yyyy HH mm").format(new java.util.Date (milli)).replace(" ", "_").replace("/", "-");
//    }
//}
