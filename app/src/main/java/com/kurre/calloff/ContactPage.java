package com.kurre.calloff;

import android.content.Context;
import android.content.Intent;
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

import java.util.HashMap;
import java.util.List;

public class ContactPage extends AppCompatActivity implements View.OnClickListener {

    EditText etEnterMessage;
    Button btnSend, btnCall;
    Contact contact;
    ListView lvMessageList;
    MyMessageListAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_page);

        etEnterMessage = (EditText) findViewById(R.id.etEnterMessage);
        btnSend = (Button) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(this);
        btnCall = (Button) findViewById(R.id.btnCall);
        btnCall.setOnClickListener(this);

        contact = getIntent().getExtras().getParcelable("contact_name");
        Toast.makeText(this, contact.name, Toast.LENGTH_SHORT).show();

        lvMessageList = (ListView) findViewById(R.id.lvChatHistory);
        lvMessageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), ((Message)parent.getItemAtPosition(position)).message, Toast.LENGTH_SHORT).show();
            }
        });

        List<Message> lMessages = MainActivity.myDbHelper.readMessageFrom(contact.phone_number);

        myAdapter = new MyMessageListAdapter(this, R.layout.contact_list_entry, lMessages);
        lvMessageList.setAdapter(myAdapter);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSend) {
            String msg = etEnterMessage.getText().toString();
            final Message message = new Message(MainActivity.PHONE_NUMBER, contact.phone_number, "out", msg);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    MainActivity.myMessanger.send_message(message);
                }
            }).start();
            myAdapter.addMessage(message);
            myAdapter.notifyDataSetChanged();
            etEnterMessage.setText("");
        } else if (v.getId() == R.id.btnCall) {
            Intent intent = new Intent(getApplicationContext(), Call.class);
            intent.putExtra("contact_name", contact);
            startActivity(intent);
            //finish(); no need to finish current activity;
        }
    }

    @Override
    protected void onDestroy() {
        System.out.println("On destroy called");
        super.onDestroy();
    }

    public void refreshContactChat() {
        List<Message> lMessages = MainActivity.myDbHelper.readMessageFrom(contact.phone_number);
        HashMap<Integer, Message> messages = new HashMap<>();
        for (int i = 0; i < lMessages.size(); ++i) {
            messages.put(i, lMessages.get(i));
        }
        myAdapter.mMessages = messages;
        myAdapter.notifyDataSetChanged();

    }

    private class MyMessageListAdapter extends ArrayAdapter<Message> {

        public HashMap<Integer, Message> mMessages = new HashMap<>();

        public MyMessageListAdapter(Context context, int resource, List<Message> objects) {
            super(context, resource, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mMessages.put(i, objects.get(i));
            }
        }

        public void addMessage(Message message) {
            mMessages.put(mMessages.size(), message);
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
