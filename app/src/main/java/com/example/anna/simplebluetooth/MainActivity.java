package com.example.anna.simplebluetooth;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    Button btnFreeTune, btnSavedTunes, btnStringUp, btnConnect ;


    private static final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
    BluetoothDevice device = null;
    BluetoothDevice choosenDevice = null;
    boolean found;

    BluetoothSocket btSocket = null;

    //Popup Window
    Dialog epicDialog;
    ImageView imgClosePopup;
    ListView lvDevices;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        epicDialog = new Dialog(this);

        btnFreeTune = (Button) findViewById(R.id.btnFreeTune);
        btnSavedTunes = (Button) findViewById(R.id.btnSavedTunes);
        btnStringUp = (Button) findViewById(R.id.btnStringUp);

        btnConnect = (Button) findViewById(R.id.btnConnect);

       // lvDevices = (ListView)findViewById(R.id.lvDevices);





        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                showPopupWindow();




            }
        });

        btnFreeTune.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // SendString("I AM ALIVE");
                Intent i2 = new Intent(MainActivity.this, NewTuneActivity.class);
                startActivity(i2);
            }
        });

        btnSavedTunes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i3 = new Intent(MainActivity.this, SavedTunesActivity.class);
                startActivity(i3);
            }
        });

        btnStringUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doDeleteAll();
            }
        });

    }

    private void doDeleteAll() {
        SharedPreferences sp =PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();

        editor.clear().apply();

    }

    public void SendString(String s){
        try {
            btSocket.getOutputStream().write(s.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ConnectESP(){
        if (pairedDevices.isEmpty()) {

            Toast.makeText(getApplicationContext(), "Please Pair the Device first", Toast.LENGTH_SHORT).show();

        } else {

            for (BluetoothDevice iterator : pairedDevices) {

                if (iterator.getAddress().toString().equals("30:AE:A4:75:5B:1E")){

                    device = iterator; //device is an object of type BluetoothDevice
                    found = true;

                    //Toast.makeText(getApplicationContext(), "Now Connected", Toast.LENGTH_LONG).show();

                    break;
                }
            }
        }


        try {
            btSocket = device.createRfcommSocketToServiceRecord(PORT_UUID);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            btSocket.connect();
            Toast.makeText(getApplicationContext(), "Now Connected", Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    public void getPairedDevices(){

        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(),"Device does not Support Bluetooth",Toast.LENGTH_SHORT).show();
        }

        if (!bluetoothAdapter.isEnabled()){
            //Ask to the user turn the bluetooth on
            Intent enableAdapter  = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableAdapter ,1);
        }

        final ArrayList list = new ArrayList();

        if(pairedDevices.size()>0){
            for (BluetoothDevice bd : pairedDevices){
                list.add(bd.getName());
                Toast.makeText(getApplicationContext(), bd.getAddress(), Toast.LENGTH_SHORT).show();

                // list.add(bd.getName() + "\n" + bd.getAddress());
            }
        }else{
            Toast.makeText(getApplicationContext(), "No Paired Bluetooth Devices", Toast.LENGTH_LONG).show();
        }

        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,list);
        lvDevices.setAdapter(adapter);

        lvDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String deviceName = list.get(position).toString();

                for (BluetoothDevice bd : pairedDevices){
                   if(bd.getName().equals(deviceName)){

                   }

                }
            }
        });

/*
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
              Object choosenDevice = list.get(position);
            }
        });
*/


    }

    public void showPopupWindow(){
        epicDialog.setContentView(R.layout.popup_window_devices);
        imgClosePopup = (ImageView) epicDialog.findViewById(R.id.imgClosePopup);
        lvDevices = (ListView) epicDialog.findViewById(R.id.lvDevices);

        getPairedDevices();

        imgClosePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                epicDialog.dismiss();
            }
        });


        epicDialog.setCanceledOnTouchOutside(false);

        epicDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        epicDialog.show();

    }




}
