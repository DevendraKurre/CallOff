package com.kurre.calloff;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by kurre on 31-10-2016.
 */
public class NewMessanger {

    Thread reader_thread = null;

    private static NewMessanger myMessanger;
    private ServerSocket serverSocket;

    private NewMessanger() {
        try {
            serverSocket = new ServerSocket(Constants.port_for_media_file_transfer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static NewMessanger getNewMessanger() {
        if(myMessanger == null)
            myMessanger = new NewMessanger();
        return myMessanger;
    }

    public void receiveFile(final String path) {
        reader_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = serverSocket.accept();
                    InputStream in = socket.getInputStream();
                    OutputStream out = new FileOutputStream(path);
                    byte[] bytes = new byte[16 * 1024];
                    int count;
                    while ((count = in.read(bytes)) > 0) {
                        out.write(bytes, 0, count);
                    }
                    out.close();
                    in.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    reader_thread.interrupt();
                }
            }
        });
        reader_thread.start();
        System.out.println("Server Started");
    }

    public void send_file(String receiver, String filePath) {
        try {
            File file = new File(filePath);
            long length = file.length();
            byte[] bytes = new byte[16 * 1024];
            InputStream in = new FileInputStream(file);
            Socket clientSocket = new Socket(receiver, Constants.port_for_media_file_transfer);
            OutputStream out = clientSocket.getOutputStream();
            int count;
            while ((count = in.read(bytes)) > 0) {
                out.write(bytes, 0, count);
            }
            out.close();
            in.close();
            clientSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
