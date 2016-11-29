package com.example.uri3l.myapplication.PersonController;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uri3l.myapplication.Database.DBController;
import com.example.uri3l.myapplication.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    private DBController dbController;
    private View header;
    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v=inflater.inflate(R.layout.fragment_account, container, false);
        final EditText FirstName=(EditText) v.findViewById(R.id.UserFirstName);
        final EditText LastName=(EditText) v.findViewById(R.id.UserLastName);
        final Button saveButton=(Button) v.findViewById(R.id.saveButton);
        final TextView userFullNameFromLayout;
        //populate inputs
        SharedPreferences settings= PreferenceManager.getDefaultSharedPreferences(getContext());
        FirstName.setText(settings.getString("firstname",""));
        LastName.setText(settings.getString("lastname",""));

        dbController=new DBController();

        //header from layout: I use it for updating user details
        NavigationView navigationView=(NavigationView) v.findViewById(R.id.nav_view);
        View header=inflater.inflate(R.layout.nav_header_main,null);
        userFullNameFromLayout=(TextView) header.findViewById(R.id.FullNameText);
        userFullNameFromLayout.setText("dfs");
        saveButton.setOnClickListener(new View.OnClickListener() {
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
                final ProgressDialog progDailog = ProgressDialog.show(
                       getActivity(), "Save user details", "Saving details.....", true);
                new Thread() {
                    public void run() {
                        try {
                            SharedPreferences settings= PreferenceManager.getDefaultSharedPreferences(getContext());
                            int UserID=settings.getInt("id",0);
                            int res=dbController.SaveUserDetails(UserID,
                                                    FirstName.getText().toString(),
                                                    LastName.getText().toString()
                                                );
                            Thread.sleep(1000);
                            switch(res){
                                case 1:
                                    getActivity().runOnUiThread(new Runnable() {
                                        public void run() {
                                            Toast.makeText(getActivity(), "Success !", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                    SharedPreferences.Editor editor = settings.edit();
                                    editor.putString("firstname", FirstName.getText().toString());
                                    editor.putString("lastname", LastName.getText().toString());
                                    editor.commit();
                                    //userFullNameFromLayout.setText("Full Name: "+FirstName.getText().toString()+" "+LastName.getText().toString());
                                    break;
                                case 0:
                                    getActivity().runOnUiThread(new Runnable() {
                                        public void run() {
                                            Toast.makeText(getActivity(), "Server error ! ", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                    break;
                                case -1:
                                    getActivity().runOnUiThread(new Runnable() {
                                        public void run() {
                                            Toast.makeText(getActivity(), "Request Error ! Please try again...", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                    break;
                            }
                        } catch (Exception e) {
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                progDailog.dismiss();
                            }
                        });
                    }
                }.start();
            }
        });
        return v;
    }

}
