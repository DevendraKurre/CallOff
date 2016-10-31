package com.kurre.calloff;

import java.util.Date;

/**
 * Created by kurre on 27-09-2016.
 */
public class Message {

    public String sender;
    public String reciepient;
    public String message;
    public int messageLength = 0;
    public String timestamp;
    public int messageType = Header.MESSAGE;
    public String messageDirection;

    public Message(String sender, String receiver, String messageDirection, String message) {
        this.sender = sender;
        this.reciepient = receiver;
        this.timestamp = new Date().toString();
        this.messageDirection = messageDirection;
        this.message = message;
        this.timestamp = new Date().toString();
    }

    public Message(String sender, String receiver, String messageDirection, int messgeLength) {
        this.sender = sender;
        this.reciepient = receiver;
        this.timestamp = new Date().toString();
        this.messageDirection = messageDirection;
        this.messageLength = messgeLength;
    }

    public Message(String sender, String receiver, String messageDirection, String timestamp, String message, int type) {
        this.sender = sender;
        this.reciepient = receiver;
        this.timestamp = timestamp;
        this.messageDirection = messageDirection;
        this.message = message;
        this.messageType = type;
    }
}
