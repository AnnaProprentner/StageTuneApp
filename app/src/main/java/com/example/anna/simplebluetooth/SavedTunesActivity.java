package com.example.anna.simplebluetooth;

import android.app.Activity;
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

import static java.sql.DriverManager.println;

public class SavedTunesActivity extends AppCompatActivity{

    Button btnTuneOne;
    String key = "";

    List<String> tuneKeyList = new ArrayList<>();

    Long number;

    // SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_tunes);

        number = doLoadLong("Counter");
        Toast.makeText(getApplicationContext(), "" + number, Toast.LENGTH_LONG).show();
       // btnTuneOne = (Button) findViewById(R.id.btnTuneOne);

        for(int i = 1; i<=number; i++){
            tuneKeyList.add("Tune" + i);
           // Log.d("KEYLIST",tuneKeyList.get(i));
        }




        if(getIntent().hasExtra("Key") == true){
            key = getIntent().getExtras().getString("Key");
        }

        GridView gridView = (GridView) findViewById(R.id.gridView);
        GridViewAdapter adapter = new GridViewAdapter(tuneKeyList,this);
        gridView.setAdapter(adapter);

        /*
        btnTuneOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnTuneOne.setText(key);

                Toast.makeText(getApplicationContext(), doLoad(key),Toast.LENGTH_LONG).show();

            }
        });

        */
    }

    public String doLoad(String key){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String str = sp.getString(key, "EMPTY");

       // String[] stringArray = str.split(":");

        return str;

    }

    public Long doLoadLong(String key){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        Long i = sp.getLong(key, 0);

        // String[] stringArray = str.split(":");

        return i;

    }
}
