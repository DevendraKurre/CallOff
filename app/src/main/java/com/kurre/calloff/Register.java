package com.kurre.calloff;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Register extends AppCompatActivity implements View.OnClickListener{

    EditText etUserName, etPassword, etRePassword, etMobileNumber;
    TextView tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUserName= (EditText) findViewById(R.id.etUserName);
        etPassword= (EditText) findViewById(R.id.etPassword);
        etRePassword= (EditText) findViewById(R.id.etRePassword);
        etMobileNumber= (EditText) findViewById(R.id.etMobileNumber);
        tvRegister= (TextView) findViewById(R.id.tvRegister);

        //TODO verify password by comparing password and re-password

        tvRegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.tvRegister) {
            Worker worker = new Worker(this, Constants.task.REGISTER);
            worker.execute(Constants.REGISTER_URL + "?user_name=" +etUserName.getText() + "&password=" + etPassword.getText() + "&mobile_number=" + etMobileNumber.getText() );
        }
    }
}

