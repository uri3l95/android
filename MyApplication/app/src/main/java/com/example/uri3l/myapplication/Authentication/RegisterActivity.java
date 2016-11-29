package com.example.uri3l.myapplication.Authentication;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.uri3l.myapplication.Database.DBController;

import com.example.uri3l.myapplication.R;

public class RegisterActivity extends Activity {
    DBController dbController;
    private boolean isOnline(){
        ConnectivityManager cm=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo=cm.getActiveNetworkInfo();
        return netInfo!=null && netInfo.isConnectedOrConnecting();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final EditText FirstName=(EditText) findViewById(R.id.FirstName);
        final EditText LastName=(EditText) findViewById(R.id.LastName);
        final EditText Username=(EditText) findViewById(R.id.Username);
        final EditText Password=(EditText) findViewById(R.id.Password);

        final Button registerButton=(Button) findViewById(R.id.registerButton);
        dbController=new DBController();
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FirstName.getText().toString().trim().equals("")){
                    FirstName.setError( "First Name is required!" );
                    return;
                }
                if(LastName.getText().toString().trim().equals("")){
                    LastName.setError( "Last Name is required!" );
                    return;
                }
                if(Username.getText().toString().trim().equals("")){
                    Username.setError( "Username is required!" );
                    return;
                }
                if(Password.getText().toString().trim().equals("")){
                    Password.setError( "Password is required!" );
                    return;
                }
                if(isOnline()) {
                    final ProgressDialog progDailog = ProgressDialog.show(
                            RegisterActivity.this, "Login", "Loading...", true);
                    final Toast toast = Toast.makeText(RegisterActivity.this, "Login Failed ! Username or password wrong !", Toast.LENGTH_LONG);
                    new Thread() {
                        public void run() {
                            try {
                                int result = dbController.register_user(FirstName.getText().toString(),
                                                                        LastName.getText().toString(),
                                                                        Username.getText().toString(),
                                                                        Password.getText().toString());
                                Thread.sleep(1500);
                                switch(result){
                                    case 1:
                                        //redirect to login page
                                        Intent afterLogin = new Intent(RegisterActivity.this, LoginActivity.class);
                                        RegisterActivity.this.startActivity(afterLogin);
                                        break;
                                    case 0:
                                        runOnUiThread(new Runnable() {
                                            public void run() {
                                                Toast.makeText(RegisterActivity.this, "Username already exist!", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                        break;
                                    case -1:
                                        runOnUiThread(new Runnable() {
                                            public void run() {
                                                Toast.makeText(RegisterActivity.this, "Request Error !", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                        break;
                                }
                            } catch (Exception e) {
                            }
                            progDailog.dismiss();
                        }
                    }.start();
                }else{
                    Toast.makeText(RegisterActivity.this, "Please check your network connection... !", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
