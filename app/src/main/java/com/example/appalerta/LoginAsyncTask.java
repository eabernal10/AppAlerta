package com.example.appalerta;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Entity;

import java.io.IOException;

public abstract class LoginAsyncTask extends AsyncTask<Void, Void, String> {
    private Activity activity;
    private ProgressDialog dialog;
    private String username,password;

    public LoginAsyncTask(Activity activity, String username, String password) {
        this.activity = activity;
        this.username = username;
        this.password = password;
        this.dialog = new ProgressDialog(activity);
    }
    @Override
    protected void onPreExecute(){
        this.dialog.setMessage("Cargando....");
        this.dialog.setTitle("App Alerta");
        this.dialog.setCancelable(false);
        this.dialog.show();
    }
    @Override
    protected  String doInBackground(Void... voids){
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("https://www.inkadroid.com/inkadroid/webservice/acces2.php?usuario="+username+"'&password='"+password);
        try {
            HttpResponse response = httpClient.execute(httpPost);
            String str;
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                str = EntityUtils.toString(response.getEntity());
            } else {
                str = "error";
            }
            return str;
        } catch (ClientProtocolException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected  void onPostExecute(String result){
        this.dialog.dismiss();
        try{
            if (!result.equalsIgnoreCase("error")){
                JSONArray jsonArray = new JSONArray(result);
                JSONObject json = jsonArray.getJSONObject(0);
                int status = json.getInt("logstatus");
                Toast.makeText(activity.getApplicationContext(),""+status, Toast.LENGTH_LONG).show();
                if (status == 1){
                    Intent i = new Intent((Login)activity, MainActivity.class);
                    activity.startActivity(i);
                    activity.finish();
                } else {
                    Toast.makeText(activity.getApplicationContext(),"Incorrecto Usuario o Contraseña", Toast.LENGTH_LONG).show();
                }
            }else {
                Toast.makeText(activity.getApplicationContext(),"Incorrecto Usuario o Contraseña", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
    }
}
