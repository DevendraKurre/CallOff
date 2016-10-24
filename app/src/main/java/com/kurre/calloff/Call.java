package com.kurre.calloff;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Call extends AppCompatActivity implements View.OnClickListener {

    Contact contact;
    Button btn_end_call, btn_answer_call, btn_reject_call;
    public static boolean isCallConnected = false;
    public static Caller caller = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        contact = getIntent().getExtras().getParcelable("contact_name");
        String direction = getIntent().getExtras().getString("call_direction");

        MainActivity.callActivity = this;
        caller = new Caller(contact.ip_address);

        btn_end_call = (Button) findViewById(R.id.btn_end_call);
        btn_end_call.setOnClickListener(this);
        btn_answer_call = (Button) findViewById(R.id.btn_answer_call);
        btn_answer_call.setOnClickListener(this);
        btn_reject_call = (Button) findViewById(R.id.btn_reject_call);
        btn_reject_call.setOnClickListener(this);

        if("out".equals(direction)) {
            btn_end_call.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Going to call " + contact.name, Toast.LENGTH_SHORT).show();
            final Message message = new Message(MainActivity.PHONE_NUMBER, contact.phone_number, "out", "");
            message.messageType = Header.CALL_INITIATED;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    MainActivity.myMessanger.send_message(message);
                }
            }).start();
        } else if("in".equals(direction)) {
            btn_answer_call.setVisibility(View.VISIBLE);
            btn_reject_call.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Getting call from " + contact.name, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.btn_end_call) {

            //Setting Layout
            btn_end_call.setVisibility(View.INVISIBLE);

            //Sending Response to other party
            final Message message = new Message(MainActivity.PHONE_NUMBER, contact.phone_number, "out", "");
            message.messageType = Header.CALL_DISCONNECT;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    MainActivity.myMessanger.send_message(message);
                }
            }).start();

            //Stopping the caller and ending the activity
            Toast.makeText(this, "Call disconnected", Toast.LENGTH_SHORT).show();
            if(isCallConnected)
                caller.stop_caller();
            endCall();
        }

        else if(v.getId() == R.id.btn_reject_call) {

            //Setting Layout
            btn_reject_call.setVisibility(View.INVISIBLE);
            btn_answer_call.setVisibility(View.INVISIBLE);

            //Sending Response to other party
            final Message message = new Message(MainActivity.PHONE_NUMBER, contact.phone_number, "out", "");
            message.messageType = Header.CALL_DISCONNECT;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    MainActivity.myMessanger.send_message(message);
                }
            }).start();
            Toast.makeText(this, "Call disconnected", Toast.LENGTH_SHORT).show();

            //Ending the activity
            endCall();
        }

        else if(v.getId() == R.id.btn_answer_call) {

            //Setting Layout
            btn_reject_call.setVisibility(View.INVISIBLE);
            btn_answer_call.setVisibility(View.INVISIBLE);
            btn_end_call.setVisibility(View.VISIBLE);

            //Sending Response to other party
            final Message message = new Message(MainActivity.PHONE_NUMBER, contact.phone_number, "out", "");
            message.messageType = Header.CALL_CONNECTED;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    MainActivity.myMessanger.send_message(message);
                }
            }).start();

            //Starting the callActivity
            Toast.makeText(this, "Call connected", Toast.LENGTH_SHORT).show();
            isCallConnected = true;
            caller.start_caller();

        }
    }

    public void startCall() {
        isCallConnected = true;
        caller.start_caller();
    }

    public void endCall() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    Call.this.finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        MainActivity.callActivity = null;
        isCallConnected = false;
    }
}
