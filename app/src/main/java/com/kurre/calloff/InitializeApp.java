package com.kurre.calloff;

import android.os.Environment;

import java.io.File;

/**
 * Created by kurre on 31-10-2016.
 */
public class InitializeApp {

    public static void initializeAll() {
        createDirectories();
    }

    private static void createDirectories() {

        System.out.println("Checkin for main directory " + Environment.getExternalStorageDirectory() + "/" + Constants.DIRECTORY_APP_MAIN);
        File mainDirectory = new File(Environment.getExternalStorageDirectory() + "/" + Constants.DIRECTORY_APP_MAIN);
        if(mainDirectory.exists() && mainDirectory.isDirectory()) {
            System.out.println("App directory exists, no need to create create again");
        } else {
            Boolean result_1 = mainDirectory.mkdirs();
            mainDirectory = new File(Environment.getExternalStorageDirectory() + "/" + Constants.DIRECTORY_MEDIA);
            Boolean result_2 = mainDirectory.mkdirs();
            mainDirectory = new File(Environment.getExternalStorageDirectory() + "/" + Constants.DIRECTORY_AUDIO);
            Boolean result_3 = mainDirectory.mkdirs();
            mainDirectory = new File(Environment.getExternalStorageDirectory() + "/" + Constants.DIRECTORY_AUDIO_SENT);
            Boolean result_4 = mainDirectory.mkdirs();
            System.out.println("Directory Structure for callOff app is created successfully " + result_1 + "-" + result_2 + "-" + result_3 + "-" + result_4);
        }
    }
}
