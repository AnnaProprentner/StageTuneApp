package com.example.anna.simplebluetooth;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class SavedTunesActivity extends AppCompatActivity {
    List<String> tuneKeyList = new ArrayList<>();

    int number, count;

    Dialog epicDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_tunes);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();

        Map<String, ?> allTunes = sp.getAll();
        tuneKeyList = new ArrayList<String>(allTunes.keySet());

        epicDialog = new Dialog(this);

        GridView gridView = (GridView) findViewById(R.id.gridView);
        GridViewAdapter adapter = new GridViewAdapter(tuneKeyList, this, epicDialog, SavedTunesActivity.this);
        gridView.setAdapter(adapter);
    }
}
