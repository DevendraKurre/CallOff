package com.kurre.calloff;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

/**
 * A login screen that offers login via email/password.
 */
public class Login extends Activity implements OnClickListener {

    EditText etUserName,etPassword;
    TextView tvLogin,tvSign,tvLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etPassword= (EditText) findViewById(R.id.etPassword);
        etUserName= (EditText) findViewById(R.id.etUserName);
        tvLogin= (TextView) findViewById(R.id.tvLogin);
        tvSign= (TextView) findViewById(R.id.tvForgotPass);
        tvLink=(TextView) findViewById(R.id.tvRegister);

        tvLogin.setOnClickListener(this);
        tvSign.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Worker worker = null;
        switch(v.getId())
        {
            case R.id.tvLogin:
                worker = new Worker(this, Constants.task.LOGIN);
                //worker.execute(Constants.LOGIN_URL + "?user_name=" +etUserName.getText() + "&password=" + etPassword.getText() );
                worker.execute(Constants.LOGIN_URL + "?user_name=admin&password=admin");
                break;
            case R.id.tvRegister:

                break;
            case R.id.tvForgotPass:

                break;

        }

    }
}

