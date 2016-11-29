package com.example.uri3l.myapplication.PersonController;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.uri3l.myapplication.Database.DBController;
import com.example.uri3l.myapplication.EmailService.EmailService;
import com.example.uri3l.myapplication.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SendEmailFragment extends Fragment {

    private DBController dbController;
    private EmailService emailService;
    public SendEmailFragment() {
        // Required empty public constructor
    }

    private boolean isOnline(){
        ConnectivityManager cm=(ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo=cm.getActiveNetworkInfo();
        return netInfo!=null && netInfo.isConnectedOrConnecting();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v= inflater.inflate(R.layout.fragment_send_email, container, false);
        final EditText Subject=(EditText) v.findViewById(R.id.Subject);
        final EditText Body=(EditText) v.findViewById(R.id.Body);
        final Spinner spinner=(Spinner) v.findViewById(R.id.personspinner);
        final Button sendButton=(Button) v.findViewById(R.id.sendButton);
        final ArrayList<String> items=getpersonsEmail();
        final ArrayAdapter<String> adapter=new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_item,items);
        spinner.setAdapter(adapter);
        dbController = new DBController();
        emailService=new EmailService();
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOnline()) {
                    if(Subject.getText().toString().trim().equals("")){
                        Subject.setError( "Subject is required!" );
                        return;
                    }
                    if(Body.getText().toString().trim().equals("")){
                        Body.setError( "Body is required!" );
                        return;
                    }
                    final ProgressDialog progDailog = ProgressDialog.show(
                            getActivity(), "Send email", "Sending.....", true);
                    new Thread() {
                        public void run() {
                            try {
                                String subject=Subject.getText().toString();
                                String body=Body.getText().toString();
                                subject=subject.replaceAll(" ","%20");
                                body=body.replaceAll(" ","%20");
                                int result = emailService.sendEmail(spinner.getSelectedItem().toString(),
                                        subject,
                                        body
                                        );
                                Thread.sleep(1000);
                                switch(result){
                                    case 1:
                                        getActivity().runOnUiThread(new Runnable() {
                                            public void run() {
                                                Toast.makeText(getActivity(), "Email send successfully!", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                        break;
                                    case -1:
                                        getActivity().runOnUiThread(new Runnable() {
                                            public void run() {
                                                Toast.makeText(getActivity(), "Request Error !", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(getActivity(), "Please check your network connection... !", Toast.LENGTH_LONG).show();
                }
            }
        });
        return v;
    }

    private ArrayList<String> getpersonsEmail() {
        //get persons for current user
        dbController = new DBController();
        SharedPreferences settings= PreferenceManager.getDefaultSharedPreferences(getContext());
        int UserID=settings.getInt("id",0);
        return dbController.mypersonsEmail(UserID);
    }

}
