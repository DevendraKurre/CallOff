package com.kurre.calloff;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ContactPage extends AppCompatActivity implements View.OnClickListener {

    EditText etEnterMessage;
    Button btnSend;
    Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_page);

        etEnterMessage = (EditText) findViewById(R.id.etEnterMessage);
        btnSend = (Button) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(this);

        contact = getIntent().getExtras().getParcelable("contact_name");
        Toast.makeText(this, contact.name, Toast.LENGTH_SHORT).show();

        ListView lvMessageList = (ListView) findViewById(R.id.lvChatHistory);
        lvMessageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), ((Message)parent.getItemAtPosition(position)).message, Toast.LENGTH_SHORT).show();
            }
        });

        List<Message> lMessages = MainActivity.myDbHelper.readMessageFrom(contact.phone_number);

        MyMessageListAdapter myAdapter = new MyMessageListAdapter(this, R.layout.contact_list_entry, lMessages);
        lvMessageList.setAdapter(myAdapter);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSend) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String msg = etEnterMessage.getText().toString();
                    Message message = new Message("" + Constants.phone_number, contact.phone_number, "out", msg);
                    MainActivity.sendMessage(message);
                }
            }).start();
        }
    }

    private class MyMessageListAdapter extends ArrayAdapter<Message> {

        public HashMap<Integer, Message> mMessages = new HashMap<>();

        public MyMessageListAdapter(Context context, int resource, List<Message> objects) {
            super(context, resource, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mMessages.put(i, objects.get(i));
            }
        }

        @Override
        public int getCount() {
            return mMessages.size();
        }

        @Override
        public Message getItem(int position) {
            return mMessages.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.contact_list_entry, null);
            }
            Message message = getItem(position);
            if (message != null) {
                TextView tvContactName = (TextView) v.findViewById(R.id.tvContactName);
                TextView tvContactNumber = (TextView) v.findViewById(R.id.tvContactNumber);
                tvContactName.setText(message.sender);
                tvContactNumber.setText(message.message);
            }
            return v;
        }
    }
}