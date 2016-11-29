package com.example.uri3l.myapplication.Database;
import android.os.StrictMode;
import com.example.uri3l.myapplication.Model.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class DBController{
    private StrictMode.ThreadPolicy policy;
    private HttpClient httpclient;
    private String serverUrl;
    public DBController(){
        this.policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(this.policy);
        this.httpclient=new DefaultHttpClient();
        this.serverUrl="http://192.168.15.102:3000";
    }
    public User login_user(final String username, final String password){
        String url = serverUrl+"/Login";
        HttpPost request=new HttpPost(url+"?username="+username+"&password="+password);
        BasicResponseHandler handler = new BasicResponseHandler();
        String result = "";
        try {
            result = httpclient.execute(request, handler);
            JSONObject toJson=new JSONObject(result);
            JSONArray user=toJson.getJSONArray("data");
                return new User(user.getJSONObject(0).getInt("ID"),
                            user.getJSONObject(0).getString("FirstName"),
                            user.getJSONObject(0).getString("LastName"),
                            user.getJSONObject(0).getString("Username"),
                            user.getJSONObject(0).getString("Password")
                );
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return null;
        }
    }
    public int register_user(final String firstname,final String lastname,final String username, final String password){
        String url = serverUrl + "/Register";
        HttpPost request=new HttpPost(url+"?firstname="+firstname+"&lastname="+lastname+"&username="+username+"&password="+password);
        BasicResponseHandler handler = new BasicResponseHandler();
        String result = "";
        try {
            result = httpclient.execute(request, handler);
            JSONObject toJson=new JSONObject(result);
            return toJson.getInt("valid");
        } catch (Exception e) {
            //for request error
            return -1;
        }
    }
    public int add_person(String firstname, String lastname, String email, int age, String phone, String country, int userid){
        String url = serverUrl+ "/AddPerson";
        HttpPost request=new HttpPost(url+"?firstname="+firstname+
                                        "&lastname="+lastname+
                                        "&email="+email+
                                        "&age="+age+
                                        "&phone="+phone+
                                        "&country="+country+
                                        "&userid="+userid);
        BasicResponseHandler handler = new BasicResponseHandler();
        String result = "";
        try {
            result = httpclient.execute(request, handler);
            JSONObject toJson=new JSONObject(result);
            return toJson.getInt("valid");
        } catch (Exception e) {
            //for request error
            return -1;
        }
    }
    public ArrayList<String> mypersons(int userId){
        String url = serverUrl+ "/Mypersons";
        HttpPost request=new HttpPost(url+"?userId="+userId);
        BasicResponseHandler handler = new BasicResponseHandler();
        ArrayList<String> personsList = new ArrayList<String>();
        String[] mypersons=new String[]{};
        String result="";
        try {
            result = httpclient.execute(request, handler);
            JSONArray jsonarray = new JSONArray(result);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                String firstname=jsonobject.getString("FirstName");
                String lastname=jsonobject.getString("LastName");
                personsList.add(firstname+" "+lastname);
            }
            return personsList;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    public ArrayList<String> mypersonsEmail(int userId){
        String url = serverUrl+ "/Mypersons";
        HttpPost request=new HttpPost(url+"?userId="+userId);
        BasicResponseHandler handler = new BasicResponseHandler();
        ArrayList<String> personsList = new ArrayList<String>();
        String[] mypersons=new String[]{};
        String result="";
        try {
            result = httpclient.execute(request, handler);
            JSONArray jsonarray = new JSONArray(result);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                String email=jsonobject.getString("Email");
                personsList.add(email);
            }
            return personsList;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    public int SaveUserDetails(int userID,String firstname,String lastname){
        String url = serverUrl+ "/SaveUser";
        HttpPost request=new HttpPost(url+"?id="+userID+
                "&firstname="+firstname+
                "&lastname="+lastname
        );
        String result="";
        BasicResponseHandler handler = new BasicResponseHandler();
        try {
            result = httpclient.execute(request, handler);
            JSONObject toJson=new JSONObject(result);
            return toJson.getInt("valid");
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
