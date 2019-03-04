package com.example.anna.simplebluetooth;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import java.util.List;

public class GridViewAdapter extends BaseAdapter {

    List<String> sourceList;
    Context myContext;

   public GridViewAdapter(List<String> sourceList, Context myContext){
        this.sourceList = sourceList;
        this.myContext = myContext;
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

                    String toast = doLoad(button.getText().toString(), myContext);
                    Toast.makeText(myContext,toast,Toast.LENGTH_SHORT).show();

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


}
