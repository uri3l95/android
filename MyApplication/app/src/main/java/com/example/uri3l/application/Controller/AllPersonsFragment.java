package com.example.uri3l.application.Controller;


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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.uri3l.application.Database.DBController;
import com.example.uri3l.myapplication.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllPersonsFragment extends Fragment {

    private static String[] PERSONS = new String[]{"ALA","BALA","CACA"};
    private DBController dbController;
    public AllPersonsFragment() {
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
        final View v=inflater.inflate(R.layout.fragment_all_persons, container, false);
        dbController=new DBController();
        SharedPreferences settings= PreferenceManager.getDefaultSharedPreferences(getContext());
        int UserID=settings.getInt("id",0);
        ArrayList<String> result = dbController.myPersons(UserID);
        PERSONS=result.toArray(new String[0]);
        ListAdapter LA=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,PERSONS);
        ListView listView=(ListView) v.findViewById(R.id.personsList);
        listView.setAdapter(LA);
        return v;
    }

}
