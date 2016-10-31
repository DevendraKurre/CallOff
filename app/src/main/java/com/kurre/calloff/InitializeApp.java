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

        System.out.println("Checkin for main directory");
        File mainDirectory = new File(Environment.getExternalStorageDirectory() + "/" + Constants.DIRECTORY_APP_MAIN);
        if(mainDirectory.exists() && mainDirectory.isDirectory()) {
            System.out.println("App directory exists, no need to create create again");
        } else {
            mainDirectory.mkdir();
            mainDirectory = new File(Environment.getExternalStorageDirectory() + "/" + Constants.DIRECTORY_MEDIA);
            mainDirectory.mkdir();
            mainDirectory = new File(Environment.getExternalStorageDirectory() + "/" + Constants.DIRECTORY_AUDIO);
            mainDirectory.mkdir();
            mainDirectory = new File(Environment.getExternalStorageDirectory() + "/" + Constants.DIRECTORY_AUDIO_SENT);
            mainDirectory.mkdir();
            System.out.println("Directory Structure for callOff app is created successfully");
        }
    }
}
