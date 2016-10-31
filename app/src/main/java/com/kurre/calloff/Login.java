package com.kurre.calloff;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

/**
 * A login screen that offers login via email/password.
 */
public class Login extends Activity implements OnClickListener {

    EditText etUserName, etPassword;
    TextView tvLogin, tvForgotPassLink, tvRegisterLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etPassword= (EditText) findViewById(R.id.etPassword);
        etUserName= (EditText) findViewById(R.id.etUserName);
        tvLogin= (TextView) findViewById(R.id.tvLogin);
        tvForgotPassLink = (TextView) findViewById(R.id.tvForgotPassLink);
        tvRegisterLink =(TextView) findViewById(R.id.tvRegisterLink);

        tvLogin.setOnClickListener(this);
        tvForgotPassLink.setOnClickListener(this);
        tvRegisterLink.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Worker worker = null;
        switch(v.getId())
        {
            case R.id.tvLogin:
                worker = new Worker(this, Constants.task.LOGIN);
                worker.execute(Constants.LOGIN_URL + "?user_name=" +etUserName.getText() + "&password=" + etPassword.getText() );
                break;
            case R.id.tvRegisterLink:
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
                //finish();
                break;
            case R.id.tvForgotPassLink:

                break;

        }

    }
}

