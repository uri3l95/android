package com.example.uri3l.application.Authentication;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uri3l.application.Database.DBController;
import com.example.uri3l.application.Model.User;
import com.example.uri3l.application.Controller.MainActivity;
import com.example.uri3l.myapplication.R;
public class LoginActivity extends Activity {

    Button loginButton;
    EditText Username,Password;
    TextView errorMessage;
    DBController dbController;
    private ProgressDialog dialog;
    private boolean isOnline(){
        ConnectivityManager cm=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo=cm.getActiveNetworkInfo();
        return netInfo!=null && netInfo.isConnectedOrConnecting();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final EditText Username=(EditText) findViewById(R.id.Username);
        final EditText Password=(EditText) findViewById(R.id.Password);
        final Button loginButton=(Button) findViewById(R.id.loginButton);
        final TextView registerLink=(TextView) findViewById(R.id.tvRegisterHere);

        dbController=new DBController();
        //register link
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent=new Intent(LoginActivity.this,RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOnline()) {
                    final ProgressDialog progDailog = ProgressDialog.show(
                            LoginActivity.this, "Login", "Loading...", true);
                    final Toast toast = Toast.makeText(LoginActivity.this, "Login Failed ! Username or password wrong !", Toast.LENGTH_LONG);
                    new Thread() {
                        public void run() {
                            try {
                                User user = dbController.login_user(Username.getText().toString(), Password.getText().toString());
                                Thread.sleep(1500);
                                if (user != null) {
                                    //set session
                                    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                    SharedPreferences.Editor editor = settings.edit();
                                    editor.putInt("id", user.getID());
                                    editor.putString("firstname", user.getFirstName());
                                    editor.putString("lastname", user.getLastName());
                                    editor.putString("username", user.getUsername());
                                    editor.putString("password", user.getPassword());
                                    editor.commit();
                                    //redirect to next page
                                    Intent afterLogin = new Intent(LoginActivity.this, MainActivity.class);
                                    LoginActivity.this.startActivity(afterLogin);
                                } else {
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            Toast.makeText(LoginActivity.this, "Login failed !", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            } catch (Exception e) {
                                progDailog.dismiss();
                            }
                            progDailog.dismiss();
                        }
                    }.start();
                }else{
                    Toast.makeText(LoginActivity.this, "Please check your network connection... !", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
