package com.example.uri3l.application.Controller;

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
import com.example.uri3l.application.Database.DBController;
import com.example.uri3l.myapplication.R;

import org.json.JSONArray;

import java.io.InputStream;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddPersonFragment extends Fragment {
    public AddPersonFragment() {

    }
    private DBController dbController;
    private boolean isOnline(){
        ConnectivityManager cm=(ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo=cm.getActiveNetworkInfo();
        return netInfo!=null && netInfo.isConnectedOrConnecting();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v=inflater.inflate(R.layout.fragment_add_persons, container, false);
        final EditText FirstName=(EditText) v.findViewById(R.id.PersonFirstName);
        final EditText LastName=(EditText) v.findViewById(R.id.PersonLastName);
        final EditText Email=(EditText) v.findViewById(R.id.Email);
        final EditText Age=(EditText) v.findViewById(R.id.PersonAge);
        final EditText Phone=(EditText) v.findViewById(R.id.PersonPhone);
        final Spinner spinner=(Spinner) v.findViewById(R.id.cspinner);
        final Button addPersonButton=(Button) v.findViewById(R.id.AddPersonButton);
        final ArrayList<String> items=getCountries("countries.json");
        final ArrayAdapter<String> adapter=new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_item,items);
        spinner.setAdapter(adapter);
        dbController=new DBController();
        addPersonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOnline()) {
                    try {
                        if (FirstName.getText().toString().trim().equals("")) {
                            FirstName.setError("First Name is required!");
                            return;
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    if(LastName.getText().toString().trim().equals("")){
                        LastName.setError( "Last Name is required!" );
                        return;
                    }
                    if(Email.getText().toString().trim().equals("")){
                        Email.setError( "Email is required!" );
                        return;
                    }
                    if(Phone.getText().toString().trim().equals("")){
                        Phone.setError( "Phone is required!" );
                        return;
                    }
                    final ProgressDialog progDailog = ProgressDialog.show(
                            getActivity(), "Add person", "Register person...", true);
                    new Thread() {
                        public void run() {
                            try {
                                SharedPreferences settings= PreferenceManager.getDefaultSharedPreferences(getContext());
                                int UserID=settings.getInt("id",0);
                                int result = dbController.add_person(FirstName.getText().toString(),
                                        LastName.getText().toString(),
                                        Email.getText().toString(),
                                        Integer.parseInt(Age.getText().toString()),
                                        Phone.getText().toString(),
                                        spinner.getSelectedItem().toString(),
                                        UserID);
                                Thread.sleep(1000);
                                switch(result){
                                    case 1:
                                        getActivity().runOnUiThread(new Runnable() {
                                            public void run() {
                                                Toast.makeText(getActivity(), "Person successfully added!", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                        break;
                                    case 0:
                                        getActivity().runOnUiThread(new Runnable() {
                                            public void run() {
                                                Toast.makeText(getActivity(), "Person already exist!", Toast.LENGTH_LONG).show();
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
    public ArrayList<String> getCountries(String filename){
        JSONArray jsonArray=null;
        ArrayList<String> cList=new ArrayList<String>();
        try{
            InputStream is=getResources().getAssets().open(filename);
            int size=is.available();
            byte[] data=new byte[size];
            is.read(data);
            is.close();
            String json=new String(data,"UTF-8");
            jsonArray=new JSONArray(json);
            if(jsonArray!=null){
                for(int i=0;i<jsonArray.length();i++){
                    cList.add(jsonArray.getJSONObject(i).getString("name"));
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return cList;
    }

}
