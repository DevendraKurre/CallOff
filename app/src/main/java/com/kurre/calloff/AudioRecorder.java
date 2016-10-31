package com.kurre.calloff;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kurre on 30-10-2016.
 */
public class AudioRecorder {

    private MediaRecorder myAudioRecorder;
    String outputFile;

    AudioRecorder(String contactNumber) {
        String timestamp = new SimpleDateFormat("yyyyMMdd-hh-mm-ss").format(new Date());
        outputFile = "/" + Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + Constants.DIRECTORY_AUDIO_SENT + "/" + timestamp + contactNumber + ".3gp";
    }

    public void startRecording() {
        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile(outputFile);

        try {
            myAudioRecorder.prepare();
            myAudioRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopRecording() {
        myAudioRecorder.stop();
        myAudioRecorder.release();
        myAudioRecorder = null;
    }
}
