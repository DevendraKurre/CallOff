package com.kurre.calloff;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static Messanger myMessanger = new Messanger();
    public static MyDatabaseHelper myDbHelper;
    ListView lvResentChatList;;
    Map<Contact, String> mRecentChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myDbHelper = new MyDatabaseHelper(this);
        myMessanger.context = this;
        myMessanger.start_server();

        lvResentChatList = (ListView) findViewById(R.id.lvResentList);
        lvResentChatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getApplicationContext(), contactName, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), ContactPage.class);
                intent.putExtra("contact_name", (Parcelable) parent.getItemAtPosition(position));
                startActivity(intent);
            }
        });
        mRecentChat = myDbHelper.readRecentChat();
        MyRecentChatListAdapter myAdapter = new MyRecentChatListAdapter(this, R.layout.contact_list_entry, new ArrayList<>(mRecentChat.keySet()));
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

    public static void loadList(String data) {
        String[] dataArray = data.substring(0, data.length()-1).split(",");

        for (String eachContact : dataArray) {
            String[] contact_details = eachContact.split("#");
            Contact newContact = new Contact(contact_details[0], contact_details[1], contact_details[2]);
            if (!ContactList.lContacts.contains(newContact))ContactList.lContacts.add(newContact);
        }
    }

    public static void sendMessage(Message message) {
        myMessanger.send_message(message);
        long result = myDbHelper.insertMessage(message);
        System.out.println("Insert result:- " + result);
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
                tvContactName.setText(contact.name);
                tvContactNumber.setText(mRecentChat.get(contact));
            }
            return v;
        }
    }

}
