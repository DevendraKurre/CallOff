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
    public static final String USER_DETAILS_GENDER = "gender";
    public static final String USER_DETAILS_PROFILE_PIC = "profile_pic";
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
    String query_create_table_message = "CREATE TABLE " + MESSAGE_TABLE + " (" +
            MESSAGE_TABLE_ID + " INTEGER PRIMARY KEY," +
            MESSAGE_TABLE_TIMESTAMP + " TEXT," +
            MESSAGE_TABLE_SENDER + " TEXT," +
            MESSAGE_TABLE_RECEIVER + " TEXT," +
            MESSAGE_TABLE_DIRECTION + " TEXT," +
            MESSAGE_TABLE_CONTENT + " TEXT" +
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
        content.put(MESSAGE_TABLE_TIMESTAMP, new Date().toString());
        content.put(MESSAGE_TABLE_DIRECTION, message.messageDirection);
        content.put(MESSAGE_TABLE_SENDER, message.sender);
        content.put(MESSAGE_TABLE_RECEIVER, message.reciepient);
        content.put(MESSAGE_TABLE_CONTENT, message.message);
        return myDb.insert(MESSAGE_TABLE, null, content);
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
            lMessages.add(new Message(sender, receiver, direction, timestamp, content));
            System.out.println(content + " -> " + sender + " -> " + receiver + " -> " + direction + " -> " + timestamp);
        }
        crcr.close();
        return lMessages;
    }

    public Map<Contact, String> readRecentChat() {
        Map<Contact, String> recentChat = new HashMap<>();
        for (Contact eachContact : ContactList.lContacts) {
            List<Message> msg = readMessageFrom(eachContact.phone_number);
            if (!msg.isEmpty())
                recentChat.put(eachContact, msg.get(0).message);
        }
        return recentChat;
    }
}
