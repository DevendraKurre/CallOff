package com.kurre.calloff;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

public class ContactPage extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    EditText etEnterMessage;
    Button btnSend, btnCall, btnVoiceMsg;
    Contact contact;
    ListView lvMessageList;
    MyMessageListAdapter myAdapter;
    AudioRecorder recorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_page);

        etEnterMessage = (EditText) findViewById(R.id.etEnterMessage);
        btnSend = (Button) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(this);
        btnCall = (Button) findViewById(R.id.btnCall);
        btnCall.setOnClickListener(this);
        btnVoiceMsg = (Button) findViewById(R.id.btnVoiceMsg);
        btnVoiceMsg.setOnClickListener(this);
        btnVoiceMsg.setOnTouchListener(this);

        contact = getIntent().getExtras().getParcelable("contact_name");
        Toast.makeText(this, contact.name, Toast.LENGTH_SHORT).show();

        MainActivity.contactPage = this;
        recorder = new AudioRecorder(contact.phone_number);

        lvMessageList = (ListView) findViewById(R.id.lvChatHistory);
        lvMessageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Message msg = ((Message)parent.getItemAtPosition(position));
                if(msg.messageType == Header.MESSAGE)
                    Toast.makeText(getApplicationContext(), msg.message, Toast.LENGTH_SHORT).show();
                else if (msg.messageType == Header.AUDIO) {
                    Toast.makeText(getApplicationContext(), "Audio file playing", Toast.LENGTH_SHORT).show();
                    MediaPlayer mp = MediaPlayer.create(ContactPage.this, Uri.parse(msg.message));
                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        public void onCompletion(MediaPlayer mp) {
                            mp.release();
                        }
                    });
                    mp.start();
                }
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
            intent.putExtra("call_direction", "out");
            startActivity(intent);
            //finish(); no need to finish current activity;
        } else if (v.getId() == R.id.btnVoiceMsg) {
            Toast.makeText(getApplicationContext(), Constants.RECORDER_BUTTON_CLICKED, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(v.getId() == R.id.btnVoiceMsg && event.getAction() == MotionEvent.ACTION_DOWN) {
            Toast.makeText(getApplicationContext(), "Button pressed", Toast.LENGTH_SHORT).show();
            recorder.startRecording();
            return true;
        } else if (v.getId() == R.id.btnVoiceMsg && event.getAction() == MotionEvent.ACTION_UP) {
            Toast.makeText(getApplicationContext(), "Button Released", Toast.LENGTH_SHORT).show();
            recorder.stopRecording();

            final Message message = new Message(MainActivity.PHONE_NUMBER, contact.phone_number, "out", recorder.outputFile);
            message.messageType = Header.AUDIO;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    MainActivity.myMessanger.send_message(message);
                }
            }).start();
            myAdapter.addMessage(message);
            myAdapter.notifyDataSetChanged();
            return true;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("On destroy called");
        MainActivity.mainPage.refreshRecentChat();
    }

    public void refreshContactChat() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                List<Message> lMessages = MainActivity.myDbHelper.readMessageFrom(contact.phone_number);
                HashMap<Integer, Message> messages = new HashMap<>();
                for (int i = 0; i < lMessages.size(); ++i) {
                    messages.put(i, lMessages.get(i));
                }
                myAdapter.mMessages = messages;
                myAdapter.notifyDataSetChanged();
            }
        });
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
                TextView tcTimeStamp = (TextView) v.findViewById(R.id.tvTimeStamp);
                String name = message.sender;
                if(name.equals(MainActivity.PHONE_NUMBER))
                    name = "Me";
                else if(ContactList.getContact(message.sender) != null)
                    name = ContactList.getContact(message.sender).name;
                tvContactName.setText(name);
                if(message.messageType == Header.MESSAGE)
                    tvContactNumber.setText(message.message);
                else
                    tvContactNumber.setText("Audio Message");
                try {
                    if (!message.timestamp.isEmpty()) {
                        //String timestamp = new SimpleDateFormat("h:mm a").format(new Date(message.timestamp));
                        String[] time = message.timestamp.split(" ")[3].split(":");
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
