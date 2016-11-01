package com.kurre.calloff;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //User details
    public static String USER_NAME;
    public static String PHONE_NUMBER;

    public static Messanger myMessanger = new Messanger();
    public static NewMessanger myMediaMessanger;
    public static Call callActivity = null;
    public static ContactPage contactPage = null;
    public static MainActivity mainPage = null;
    public static MyDatabaseHelper myDbHelper;
    public static Vibrator vibrator;

    ListView lvResentChatList;
    MyRecentChatListAdapter myAdapter;
    Map<Contact, Message> mRecentChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myDbHelper = new MyDatabaseHelper(this);
        myMessanger.context = this;
        mainPage = this;
        myMessanger.start_messanger();
        myMediaMessanger = NewMessanger.getNewMessanger();
        vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);

        lvResentChatList = (ListView) findViewById(R.id.lvResentList);
        lvResentChatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ContactPage.class);
                intent.putExtra("contact_name", (Parcelable) parent.getItemAtPosition(position));
                startActivity(intent);
            }
        });
        mRecentChat = myDbHelper.readRecentChat();
        myAdapter = new MyRecentChatListAdapter(this, R.layout.contact_list_entry, new ArrayList<>(mRecentChat.keySet()));
        lvResentChatList.setAdapter(myAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ContactList.class);
                startActivity(intent);
            }
        });
    }

    public void refreshRecentChat() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mRecentChat = MainActivity.myDbHelper.readRecentChat();
                HashMap<Integer, Contact> contacts = new HashMap<>();
                int i = 0;
                for (Contact contact : mRecentChat.keySet()) {
                    System.out.println(contact.name);
                    contacts.put(i++, contact);
                }
                myAdapter.mContacts = contacts;
                myAdapter.notifyDataSetChanged();
            }
        });
    }

    private class MyRecentChatListAdapter extends ArrayAdapter<Contact> {

        public HashMap<Integer, Contact> mContacts = new HashMap<>();

        public MyRecentChatListAdapter(Context context, int resource, List<Contact> objects) {
            super(context, resource, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mContacts.put(i, objects.get(i));
            }
        }

        @Override
        public int getCount() {
            return mContacts.size();
        }

        @Override
        public Contact getItem(int position) {
            return mContacts.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.contact_list_entry, null);
            }

            Contact contact = getItem(position);
            if (contact != null) {
                TextView tvContactName = (TextView) v.findViewById(R.id.tvContactName);
                TextView tvContactNumber = (TextView) v.findViewById(R.id.tvContactNumber);
                TextView tcTimeStamp = (TextView) v.findViewById(R.id.tvTimeStamp);
                tvContactName.setText(contact.name);
                if(mRecentChat.get(contact).messageType == Header.AUDIO)
                    tvContactNumber.setText("Audio Message");
                else
                    tvContactNumber.setText(mRecentChat.get(contact).message);
                try {
                    if (!mRecentChat.get(contact).timestamp.isEmpty()) {
                        //String timestamp = new SimpleDateFormat("h:mm a").format(new Date(mRecentChat.get(contact).timestamp));
                        String[] time = mRecentChat.get(contact).timestamp.split(" ")[3].split(":");
                        String timestamp = time[0] + ":" + time[1] + " " + (Integer.valueOf(time[0]) < 12 ? "am" : "pm");
                        tcTimeStamp.setText(timestamp);
                    }
                } catch(IllegalArgumentException ex) {
                    tcTimeStamp.setText("");
                }
            }
            return v;
        }
    }

}

