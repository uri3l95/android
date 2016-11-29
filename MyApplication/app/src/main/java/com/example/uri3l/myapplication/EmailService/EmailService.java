package com.example.uri3l.myapplication.EmailService;

import android.os.StrictMode;

import com.example.uri3l.myapplication.Model.User;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

/**
 * Created by uri3l on 11/9/2016.
 */

public class EmailService {
    private StrictMode.ThreadPolicy policy;
    private HttpClient httpclient;
    private String serverUrl;
    public EmailService(){
        this.policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(this.policy);
        this.httpclient=new DefaultHttpClient();
        this.serverUrl="http://192.168.15.102:3000";
    }
    public int sendEmail(final String email,final String subject, final String body){
        String url = serverUrl+"/SendEmail";
        HttpPost request=new HttpPost(url+"?email="+email+"&subject="+subject+"&body="+body);
        BasicResponseHandler handler = new BasicResponseHandler();
        String result = "";
        try {
            result = httpclient.execute(request, handler);
            JSONObject toJson=new JSONObject(result);
            return toJson.getInt("valid");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return -1;
        }
    }
}
