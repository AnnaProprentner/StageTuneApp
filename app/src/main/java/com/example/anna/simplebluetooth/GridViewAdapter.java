package com.example.anna.simplebluetooth;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class GridViewAdapter extends BaseAdapter {

    List<String> sourceList;
    Context myContext;
    Dialog epicDialog;
    Activity myActivity;

    //Popup Window

    Button btnStartTuning;
    ImageView imgClosePopup,imgDelete;
    TextView txtSelectedTune, txtTuneName;



   public GridViewAdapter(List<String> sourceList, Context myContext, Dialog epicDialog, Activity myActivity){
        this.sourceList = sourceList;
        this.myContext = myContext;
        this.epicDialog = epicDialog;
        this.myActivity = myActivity;


    }

    @Override
    public int getCount() {
        return sourceList.size();
    }

    @Override
    public Object getItem(int position) {
        return sourceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Button button;



        if(convertView == null){
            button = new Button(myContext);

            button.setLayoutParams(new GridView.LayoutParams(522,522));

            button.setPadding(5,5,5,5);
            button.setText(sourceList.get(position));

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String tuneName = button.getText().toString();
                    String selectedTune = doLoad(tuneName,myContext);


                    showPopUpWindow(selectedTune,tuneName);



                    //String toast = doLoad(button.getText().toString(), myContext);
                    //Toast.makeText(myContext,toast,Toast.LENGTH_SHORT).show();

                }
            });
        }else{
            button = (Button)convertView;


        }
        return button;
    }

    public String doLoad(String key, Context myContext){

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(myContext);
        String str = sp.getString(key, "EMPTY");

        // String[] stringArray = str.split(":");

        return str;

    }

    public void showPopUpWindow(final String selectedTune, final String tuneName){

        epicDialog.setContentView(R.layout.popup_window_saved_tunes);


        btnStartTuning = (Button) epicDialog.findViewById(R.id.btnStartTuningSavedTune);
        txtSelectedTune = (TextView) epicDialog.findViewById(R.id.txtSelectedTune);
        txtTuneName = (TextView) epicDialog.findViewById(R.id.txtName);
        imgClosePopup = (ImageView) epicDialog.findViewById(R.id.imgClosePopupSavedTunes);
        imgDelete = (ImageView) epicDialog.findViewById(R.id.imgDelete);

        txtSelectedTune.setText(selectedTune);
        txtTuneName.setText(tuneName);

        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(myContext,tuneName + " was deleted.",Toast.LENGTH_SHORT).show();

                SharedPreferences sp =PreferenceManager.getDefaultSharedPreferences(myContext);
                SharedPreferences.Editor editor = sp.edit();

                editor.remove(tuneName);
                editor.apply();
            }
        });

        btnStartTuning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String[] selectedTuneArray = selectedTuneToArray(selectedTune);

                /*
                for (int i = 0; i < selectedTuneArray.length; i++){
                    Toast.makeText(myContext,selectedTuneArray[i],Toast.LENGTH_SHORT).show();
                }
                */

               Intent i5 = new Intent(myActivity, NowTuningActivity.class);
               i5.putExtra("Tune", selectedTuneArray);
               myActivity.startActivity(i5);
            }
        });

        imgClosePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                epicDialog.dismiss();
            }
        });



        epicDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        epicDialog.show();

    }

    public String[] selectedTuneToArray(String selectedTune){
        String[] selectedTuneArray = selectedTune.split(" ");
        return  selectedTuneArray;
    }

}
