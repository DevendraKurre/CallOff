package com.kurre.calloff;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.Date;

/**
 * Created by kurre on 26-09-2016.
 */
public class Messanger {

    public MyDatabaseHelper myDbHelper;

    DatagramSocket server_socket, client_msg_socket = null;
    Thread reader_thread = null;
    public Context context;

    public void start_messanger() {
        try {
            myDbHelper = new MyDatabaseHelper(context);
            server_socket = new DatagramSocket(Constants.port_for_sending);
            client_msg_socket = new DatagramSocket(Constants.port_for_receiving);
            reader_thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while(!Thread.currentThread().isInterrupted())
                    {
                        Message message = read_header();
                        if (message.messageType == Header.MESSAGE) {
                            final String response = read_message(message.messageLength);
                            message.message = response;
                            myDbHelper.insertMessage(message);
                            System.out.println(response);
                        } else if (message.messageType == Header.CALL) {
                            //Starting Call activity on receiving new call header
                            Intent intent = new Intent(context, ContactPage.class);
                            intent.putExtra("contact_name", ContactList.getContact(message.sender));
                            context.startActivity(intent);
                            //finish();
                        }
                    }
                    client_msg_socket.close();
                }
            });
            reader_thread.start();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void stop_messanger() {
        reader_thread.interrupt();
        server_socket.close();
        System.out.println("Server stopped successfully");
    }

    public Message read_header() {
        byte[] buffer = new byte[Header.HEADER_LEN];
        try {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            client_msg_socket.receive(packet);
            buffer = packet.getData();
            ByteBuffer readableBuffer = ByteBuffer.wrap(buffer);
            Long sender = readableBuffer.getLong();
            Long receiver = readableBuffer.getLong();
            int content_type = readableBuffer.getInt();
            int data_length = readableBuffer.getInt();
            System.out.println("Senders Mobile number is: " + sender);
            System.out.println("Operation type is: " + content_type);
            System.out.println("Data length is: " + data_length);
            return new Message(sender.toString(), receiver.toString() + "", "in", data_length);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String read_message(int data_length) {
        byte[] buffer = new byte[data_length];
        try {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            client_msg_socket.receive(packet);
            buffer = packet.getData();
            String data = new String(buffer);
            System.out.println("Data received is: " + data);
            return data;
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return "No data received";
    }

    public void send_header(Message message) {
        try {
            ByteBuffer header = ByteBuffer.allocate(Header.HEADER_LEN);
            header.putLong(Long.parseLong(message.sender));
            header.putLong(Long.parseLong(message.reciepient));
            header.putInt(Header.MESSAGE).putInt(message.message.getBytes().length);
            DatagramPacket packet = new DatagramPacket(header.array(), Header.HEADER_LEN,
                    InetAddress.getByName(ContactList.getContact(message.reciepient).ip_address),
                    Constants.port_for_receiving);
            server_socket.send(packet);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    public void send_message(Message message) {
        try {
            send_header(message);
            if(message.messageType != Header.CALL) {
                DatagramPacket packet = new DatagramPacket(message.message.getBytes(), message.message.getBytes().length,
                        InetAddress.getByName(ContactList.getContact(message.reciepient).ip_address),
                        Constants.port_for_receiving);
                server_socket.send(packet);
                myDbHelper.insertMessage(message);
            }
            System.out.println("data sent");
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}

/***
 * Header format
 * 1st 8 bytes represents sender's mobile number
 * 2nd 8 bytes represent receiver's mobile number
 * next 4 bytes contains type of content (e.g 1 (CALL) or 2 (MESSAGW))
 * next 4 bytes represents length of message content in case of MESSAGE operation
 */
class Header {
    public static int HEADER_LEN = 24;

    //Operations type
    public static int MESSAGE = 1;
    public static int CALL = 2;
}
