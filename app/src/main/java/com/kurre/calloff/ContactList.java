package com.kurre.calloff;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

public class ContactList extends AppCompatActivity {

    public static ArrayList<Contact> lContacts = new ArrayList<>();

    ListView lvContactsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lvContactsList = (ListView) findViewById(R.id.lvContactsList);
        lvContactsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ContactPage.class);
                intent.putExtra("contact_name", (Contact) parent.getItemAtPosition(position));
                startActivity(intent);
                finish();
            }
        });

        MyContactListAdapter myAdapter = new MyContactListAdapter(this, R.layout.contact_list_entry, lContacts);
        lvContactsList.setAdapter(myAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public static Contact getContact(String number) {
        for (Contact eachContact : lContacts) {
            if (eachContact.phone_number.equals(number)) return eachContact;
        }
        return null;
    }

    private class MyContactListAdapter extends ArrayAdapter<Contact> {

        public HashMap<Integer, Contact> mContacts = new HashMap<>();

        public MyContactListAdapter(Context context, int resource, List<Contact> objects) {
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
                tvContactNumber.setText(contact.phone_number);
                tcTimeStamp.setText("");
            }
            return v;
        }
    }
}
