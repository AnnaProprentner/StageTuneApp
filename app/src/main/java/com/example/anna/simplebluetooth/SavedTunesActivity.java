package com.example.anna.simplebluetooth;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.sql.DriverManager.println;

public class SavedTunesActivity extends AppCompatActivity{

    String key = "";

    List<String> tuneKeyList = new ArrayList<>();

    int number,count;

    Dialog epicDialog;


     //SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_tunes);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();

        //tuneKeyList = getAllKeys();

        Map<String, ?> allTunes = sp.getAll();
        tuneKeyList = new ArrayList<String>(allTunes.keySet());

        for (String k: tuneKeyList) {
            Toast.makeText(getApplicationContext(),k ,Toast.LENGTH_SHORT).show();

        }

        epicDialog = new Dialog(this);


        /*
        count = doLoadInt("Counter");
        for(int i = 1; i<=count; i++){
            tuneKeyList.add("Tune" + i);
        }


        if(getIntent().hasExtra("Key") == true){
            key = getIntent().getExtras().getString("Key");
        }
 */


        GridView gridView = (GridView) findViewById(R.id.gridView);
        GridViewAdapter adapter = new GridViewAdapter(tuneKeyList,this,epicDialog,SavedTunesActivity.this);
        gridView.setAdapter(adapter);


    }

    public List<String> getAllKeys(){

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        Map<String, ?> allTunes = sp.getAll();
        List<String> keys = new ArrayList<String>(allTunes.keySet());

        return keys;

    }

    public String doLoad(String key){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        String tune = sp.getString(key, "EMPTY");

       // String[] stringArray = str.split(":");

        return tune;

    }

    public int doLoadInt(String key){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        int i = sp.getInt(key, 0);

        // String[] stringArray = str.split(":");

        return i;

    }

    public Long doLoadLong(String key){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        Long i = sp.getLong(key, 0);

        // String[] stringArray = str.split(":");

        return i;

    }
}
