package com.kurre.calloff;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by kurre on 25-09-2016.
 */
public class Worker extends AsyncTask {

    ProgressDialog dialog;
    Context context = null;
    Constants.task task = null;

    public Worker(Context context, Constants.task task) {
        this.context = context;
        this.task = task;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        return getData((String) objects[0]);
    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(context);
        dialog.setTitle("Contacting Server...");
        dialog.setMessage("Please wait...");
        dialog.setIndeterminate(true);
        dialog.show();
    }

    public String getData(String urlToFetchData) {
        String pagina = "", devuelve = "No Response from server";
        URL url;
        try {
            url = new URL(urlToFetchData);
            HttpURLConnection conexion = (HttpURLConnection) url
                    .openConnection();
            System.out.println("Connection created and opened");
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conexion.getInputStream()));
            System.out.println("Starting data read");

            String linea = reader.readLine();
            while (linea != null) {
                pagina += linea;
                linea = reader.readLine();
            }
            reader.close();

            devuelve = pagina;
            conexion.disconnect();
            return devuelve;
        } catch (Exception ex) {
            return devuelve;
        }
    }

    @Override
    protected void onPostExecute(Object result) {
        dialog.dismiss();
        String response = (String) result;
        Toast toast = null;
        if (this.task == task.LOGIN) {
            response = "1";                                                 //For testing purpose making all logins successfull.
            if ("1".equals(response)) {
                Worker worker = new Worker(this.context, Constants.task.GET_CONTACTS);
                worker.execute(Constants.GET_CONTACTS_URL);
                toast = Toast.makeText(context, Constants.LOGIN_SUCCESS, Toast.LENGTH_SHORT);
            } else {
                toast = Toast.makeText(context, Constants.LOGIN_FAILED, Toast.LENGTH_SHORT);
            }
        } else if (this.task == task.REGISTER) {
            toast = Toast.makeText(context, Constants.LOGIN_FAILED, Toast.LENGTH_SHORT);
        } else if (this.task == task.RESET) {
            toast = Toast.makeText(context, Constants.LOGIN_FAILED, Toast.LENGTH_SHORT);
        } else if (this.task == task.GET_CONTACTS) {
            MainActivity.loadList(response);
            Intent intent = new Intent(this.context, MainActivity.class);
            this.context.startActivity(intent);
            ((Activity)this.context).finish();
        }
        if (toast != null) toast.show();
    }

}