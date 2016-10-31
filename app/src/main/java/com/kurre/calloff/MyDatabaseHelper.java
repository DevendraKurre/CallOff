package com.kurre.calloff;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kurre on 27-09-2016.
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "CallOff.db";

    //User Details table
    public static final String USER_DETAILS_TABLE = "user_details";
    public static final String USER_DETAILS_ID = "id";
    public static final String USER_DETAILS_USER_NAME = "user_name";
    public static final String USER_DETAILS_CONTACT_NUMBER = "contact_number";
    String query_create_table_user_details = "CREATE TABLE " + USER_DETAILS_TABLE + " (" +
            USER_DETAILS_ID + " INTEGER PRIMARY KEY," +
            USER_DETAILS_USER_NAME + " TEXT," +
            USER_DETAILS_CONTACT_NUMBER + " TEXT" +
            ")";
    String query_delete_table_user_details = "DROP TABLE IF EXISTS " + USER_DETAILS_TABLE;

    //Message table
    public static final String MESSAGE_TABLE = "message";
    public static final String MESSAGE_TABLE_ID = "id";
    public static final String MESSAGE_TABLE_TIMESTAMP = "timestamp";
    public static final String MESSAGE_TABLE_SENDER = "sender";
    public static final String MESSAGE_TABLE_RECEIVER = "receiver";
    public static final String MESSAGE_TABLE_DIRECTION = "direction";
    public static final String MESSAGE_TABLE_CONTENT = "content";
    public static final String MESSAGE_TABLE_TYPE = "type";
    String query_create_table_message = "CREATE TABLE " + MESSAGE_TABLE + " (" +
            MESSAGE_TABLE_ID + " INTEGER PRIMARY KEY," +
            MESSAGE_TABLE_TIMESTAMP + " TEXT," +
            MESSAGE_TABLE_SENDER + " TEXT," +
            MESSAGE_TABLE_RECEIVER + " TEXT," +
            MESSAGE_TABLE_DIRECTION + " TEXT," +
            MESSAGE_TABLE_CONTENT + " TEXT," +
            MESSAGE_TABLE_TYPE + " TEXT" +
            ")";
    String query_delete_table_user_message = "DROP TABLE IF EXISTS " + MESSAGE_TABLE;



    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(query_create_table_user_details);
        db.execSQL(query_create_table_message);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(query_delete_table_user_details);
        db.execSQL(query_delete_table_user_message);
        onCreate(db);
    }

    public long insertMessage(Message message) {
        SQLiteDatabase myDb = getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(MESSAGE_TABLE_TIMESTAMP, message.timestamp);
        content.put(MESSAGE_TABLE_DIRECTION, message.messageDirection);
        content.put(MESSAGE_TABLE_SENDER, message.sender);
        content.put(MESSAGE_TABLE_RECEIVER, message.reciepient);
        content.put(MESSAGE_TABLE_CONTENT, message.message);
        content.put(MESSAGE_TABLE_TYPE, message.messageType);
        long result = myDb.insert(MESSAGE_TABLE, null, content);
        myDb.close();
        return result;
    }

    public List<Message> readMessageFrom(String number) {
        SQLiteDatabase myDb = getReadableDatabase();
        String query = null;
        if (null != number)
            query = "select * from " + MESSAGE_TABLE + " where " + MESSAGE_TABLE_SENDER + "='" + number + "' or " + MESSAGE_TABLE_RECEIVER + "='" + number + "'";
        else
            query = "select * from " + MESSAGE_TABLE;
        Cursor crcr = myDb.rawQuery(query, null);
        List<Message> lMessages = new ArrayList<>();
        while (crcr.moveToNext()) {
            String content = crcr.getString(crcr.getColumnIndex(MESSAGE_TABLE_CONTENT));
            String sender = crcr.getString(crcr.getColumnIndex(MESSAGE_TABLE_SENDER));
            String receiver = crcr.getString(crcr.getColumnIndex(MESSAGE_TABLE_RECEIVER));
            String direction = crcr.getString(crcr.getColumnIndex(MESSAGE_TABLE_DIRECTION));
            String timestamp = crcr.getString(crcr.getColumnIndex(MESSAGE_TABLE_TIMESTAMP));
            int type = crcr.getInt(crcr.getColumnIndex(MESSAGE_TABLE_TYPE));
            lMessages.add(new Message(sender, receiver, direction, timestamp, content, type));
            System.out.println(content + " -> " + sender + " -> " + receiver + " -> " + direction + " -> " + timestamp);
        }
        crcr.close();
        myDb.close();
        return lMessages;
    }

    public Map<Contact, Message> readRecentChat() {
        Map<Contact, Message> recentChat = new HashMap<>();
        for (Contact eachContact : ContactList.lContacts) {
            List<Message> msg = readMessageFrom(eachContact.phone_number);
            if (!msg.isEmpty())
                recentChat.put(eachContact, msg.get(msg.size()-1));
        }
        return recentChat;
    }

    public long userLogin(String userName, String contactNumber) {
        SQLiteDatabase myDb = getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(USER_DETAILS_USER_NAME, userName);
        content.put(USER_DETAILS_CONTACT_NUMBER, contactNumber);
        long result = myDb.insert(USER_DETAILS_TABLE, null, content);
        myDb.close();
        return result;
    }

    public boolean checkActiveLogin() {
        SQLiteDatabase myDb = getReadableDatabase();
        String query = "select * from " + USER_DETAILS_TABLE;
        Cursor crcr = myDb.rawQuery(query, null);
        if(crcr.getCount() == 0) {
            crcr.close();
            myDb.close();
            return false;
        } else {
            while (crcr.moveToNext()) {
                String userName = crcr.getString(crcr.getColumnIndex(USER_DETAILS_USER_NAME));
                String contactNumber = crcr.getString(crcr.getColumnIndex(USER_DETAILS_CONTACT_NUMBER));
                MainActivity.USER_NAME = userName;
                MainActivity.PHONE_NUMBER = contactNumber;
            }
            crcr.close();
            myDb.close();
            return true;
        }
    }
}
