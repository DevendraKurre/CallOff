package com.kurre.calloff;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Created by kurre on 24-10-2016.
 */
public class Caller {

    DatagramSocket call_sender_socket, call_receiver_socket = null;
    Thread senderThread, receiverThread;
    String ip_address;

    AudioRecord recorder;

    private int sampleRate = 44100;//16000;//44100;
    private int channelConfig = AudioFormat.CHANNEL_IN_MONO;
    //private int nesdf = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    private int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    int minBufSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);
    private boolean status = true;

    Caller(String ip_address) {
        this.ip_address = ip_address;
        senderThread = new Thread(new Runnable() {
            @Override
            public void run() {
                startSendingData();
            }
        });
        receiverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                startReceivingData();
            }
        });
    }

    private void startSendingData() {
        try {

            byte[] buffer = new byte[minBufSize];

            /*MediaRecorder mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder;
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            */
            DatagramPacket packet;
            final InetAddress destination = InetAddress.getByName(ip_address);
            recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate, channelConfig, audioFormat, minBufSize*10);
            recorder.startRecording();
            while(!Thread.currentThread().isInterrupted() && !call_sender_socket.isClosed()) {
                minBufSize = recorder.read(buffer, 0, buffer.length);
                //packet = new DatagramPacket (buffer, buffer.length, destination, Constants.port_for_call_receiving);
                //call_sender_socket.send(packet);
                System.out.println("Data Send during call of length: " + minBufSize);
            }

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startReceivingData() {
        try {
            System.out.println("Starting to receive");
            byte[] receiveData = new byte[minBufSize];
            AudioTrack track = null;
            int N = minBufSize;//AudioRecord.getMinBufferSize(sampleRate,AudioFormat.CHANNEL_IN_MONO,AudioFormat.ENCODING_PCM_16BIT);
            track = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate, AudioFormat.CHANNEL_OUT_MONO, audioFormat, N*10, AudioTrack.MODE_STREAM);
            while(!Thread.currentThread().isInterrupted() && !call_receiver_socket.isClosed()) {
                System.out.println("Starting while loop to receive data");
                DatagramPacket packet = new DatagramPacket(receiveData, receiveData.length);
                System.out.println("waiting to get data");
                call_receiver_socket.receive(packet);
                System.out.println("Data received");
                receiveData = packet.getData();
                System.out.println("Data received during call of length: " + receiveData.length);
                track.write(receiveData, 0, receiveData.length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void start_caller() {
        try {
            call_sender_socket = new DatagramSocket(Constants.port_for_call_sending);
            call_receiver_socket = new DatagramSocket(Constants.port_for_call_receiving);
            senderThread.start();
            receiverThread.start();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void stop_caller() {
        senderThread.interrupt();
        receiverThread.interrupt();
        call_sender_socket.close();
        call_receiver_socket.close();
    }

}
