package com.example.uri3l.myapplication.PersonController;


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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.uri3l.myapplication.Database.DBController;
import com.example.uri3l.myapplication.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllPersonsFragment extends Fragment {

    private static String[] persons = new String[]{};
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
        final View v=inflater.inflate(R.layout.fragment_all_person, container, false);
        dbController=new DBController();
        SharedPreferences settings= PreferenceManager.getDefaultSharedPreferences(getContext());
        int UserID=settings.getInt("id",0);
        //get persons
        ArrayList<String> result = dbController.mypersons(UserID);
        persons=result.toArray(new String[0]);

        ListAdapter LA=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,persons);
        final ListView listView=(ListView) v.findViewById(R.id.personsList);
        listView.setAdapter(LA);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Toast.makeText(getActivity(), "Here is an Person", Toast.LENGTH_LONG).show();
                }catch(Exception e){
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
        return v;
    }

}
