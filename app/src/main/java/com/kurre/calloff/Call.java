package com.kurre.calloff;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class Call extends AppCompatActivity {

    Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        contact = getIntent().getExtras().getParcelable("contact_name");
        Toast.makeText(this, contact.name, Toast.LENGTH_SHORT).show();

        Message message = new Message(MainActivity.PHONE_NUMBER, contact.phone_number, "out", "");
        message.messageType = Header.CALL;
        MainActivity.myMessanger.send_message(message);
    }
}
