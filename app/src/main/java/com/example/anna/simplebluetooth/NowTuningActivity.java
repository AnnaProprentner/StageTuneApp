package com.example.anna.simplebluetooth;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class NowTuningActivity extends AppCompatActivity {

    private static final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
    BluetoothDevice device = null;
    boolean found;

    public BluetoothSocket btSocket = null;

    public InputStream myInputStream;

    Thread workerThread;

    volatile boolean stopWorker;
    byte[] readBuffer;
    int readBufferPosition;

    EditText edtSend, edtReceive;

    Button btnStart;
    Spinner spStringSelect;

    String selectedString;
    String receivedString = "";

    String[] Tune = new String[7];

    String message;

    String str = "";
    String s = "";

    boolean firstSend = true;

    //Popup Window
     Dialog epicDialog;
     ImageView imgClosePopupTuning;
     Button btnReadyPopup;
     TextView txtStringToTune;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_tuning);



        if(getIntent().hasExtra("Tune") ==true){
            Tune = getIntent().getExtras().getStringArray("Tune");
        }

        /*

        Tune[1] = "E";
        Tune[2] = "A";
        Tune[3] = "D";
        Tune[4] = "G";
        Tune[5] = "H";
        Tune[6] = "e";

        */

       // edtSend = (EditText) findViewById(R.id.edtSend);
       // edtReceive = (EditText) findViewById(R.id.edtReceive);

        btnStart = (Button) findViewById(R.id.btnStart);
        spStringSelect = (Spinner) findViewById(R.id.spStringSelect);

        epicDialog = new Dialog(this);

        GetPairedDevices();
        ConnectESP();
        ListenForData();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.string_names, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spStringSelect.setAdapter(adapter);




        btnStart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {







             // String you want to Tune
              selectedString = spStringSelect.getSelectedItem().toString();


              if(selectedString.equals("E")){
                  s = Tune[1] + ";";

              }else if(selectedString.equals("A")){
                  s = Tune[2] + ";";

              }else if(selectedString.equals("D")){
                  // SendString("D;");
                  s = Tune[3] + ";";

              }else if(selectedString.equals("G")){
                  //SendString("G;");
                  s = Tune[4] + ";";

              }else if(selectedString.equals("H")){
                  //SendString("H;");
                  s = Tune[5] + ";";

              }else if(selectedString.equals("e")){
                  //SendString("e;");
                  s = Tune[6] + ";";

              }

              SendString(s);

              ShowPopupWindowOne();


            }
        });



    }



    @Override
    protected void onPause() {
        super.onPause();
        try {
            btSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void ShowPopupWindowOne(){
         epicDialog.setContentView(R.layout.popup_window_one);
         btnReadyPopup = (Button) epicDialog.findViewById(R.id.btnReadyPopUp);
         txtStringToTune = (TextView) epicDialog.findViewById(R.id.txtStringToTune);

         txtStringToTune.setText(selectedString);

         btnReadyPopup.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 //epicDialog.dismiss();
                 ShowPopupWindowThree();
             }
         });

         epicDialog.setCanceledOnTouchOutside(false);

         epicDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
         epicDialog.show();

    }

    public void ShowPopupWindowTwo(){
        epicDialog.setContentView(R.layout.popup_window_two);

        txtStringToTune = (TextView) epicDialog.findViewById(R.id.txtStringToTune);

        txtStringToTune.setText(selectedString);

        epicDialog.setCanceledOnTouchOutside(false);

        epicDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        epicDialog.show();


    }

    public void ShowPopupWindowThree(){
        epicDialog.setContentView(R.layout.popup_window_three);

        imgClosePopupTuning = (ImageView) epicDialog.findViewById(R.id.imgClosePopupTuning);


        imgClosePopupTuning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    epicDialog.dismiss();
            }
        });

        epicDialog.setCanceledOnTouchOutside(false);

        epicDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        epicDialog.show();
    }

    public void ShowPopupWindowFour(){
        epicDialog.setContentView(R.layout.popup_window_four);

        txtStringToTune = (TextView) epicDialog.findViewById(R.id.txtStringToTune);

        txtStringToTune.setText(selectedString);



        epicDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        epicDialog.show();


    }

    public void ConnectESP(){

        if (pairedDevices.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please Pair the Device first", Toast.LENGTH_SHORT).show();
        } else {

            for (BluetoothDevice iterator : pairedDevices) {
                if (iterator.getAddress().toString().equals("30:AE:A4:75:5B:1E")){
                    device = iterator; //device is an object of type BluetoothDevice
                    found = true;
                    Toast.makeText(getApplicationContext(), "Now Connected", Toast.LENGTH_LONG).show();

                    break;
                }else{
                    Toast.makeText(getApplicationContext(), "Connection Failed", Toast.LENGTH_LONG).show();
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
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            myInputStream = btSocket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void GetPairedDevices(){

        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(),"Device doesnt Support Bluetooth",Toast.LENGTH_SHORT).show();
        }

        if (!bluetoothAdapter.isEnabled())
        {
            //Ask to the user turn the bluetooth on
            Intent enableAdapter  = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableAdapter ,1);
        }

        ArrayList list = new ArrayList();

        if(pairedDevices.size()>0){
            for (BluetoothDevice bd : pairedDevices){
                list.add(bd.getName() + "\n" + bd.getAddress());
            }
        }else{
            Toast.makeText(getApplicationContext(), "No Paired Bluetooth Devices", Toast.LENGTH_LONG).show();
        }

        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,list);
        //lvDevices.setAdapter(adapter);
        //lvDevices.setOnItemClickListener(deviceListClickListener);

    }

    public void SendString(String s){

        try {
            btSocket.getOutputStream().write(s.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void ListenForData() {

//        try {
//            myInputStream = btSocket.getInputStream();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        final Handler handler = new Handler();
        final byte delimiter = 48; //Trennzeichen ASCII == 0

        stopWorker = false;
        readBufferPosition = 0;
        readBuffer = new byte[1024];

        final String[] receivedDataBT = new String[1];

        workerThread = new Thread(new Runnable() {
            @Override
            public void run() {

                while (!Thread.currentThread().isInterrupted() && !stopWorker)

                {
                    try {

                        int bytesAvailable = myInputStream.available();

                        if(bytesAvailable > 0){

                            byte[] packetBytes = new byte[bytesAvailable];
                            myInputStream.read(packetBytes); //LOOK UP

                            for (int i = 0; i<bytesAvailable;i++){
                                byte b = packetBytes[i];

                                if(b==delimiter){
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer,0,encodedBytes,0,encodedBytes.length); //LOOK UP
                                    final String data = new String(encodedBytes,"US-ASCII");

                                    receivedString = data;

                                    readBufferPosition = 0;


                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            //edtMessage.setText(data);
                                           receivedString = data;
                                            //Toast.makeText(getApplicationContext(), receivedString, Toast.LENGTH_SHORT).show();

                                           // edtReceive.setText(data);

                                            if(receivedString.equals("close")){
                                                epicDialog.dismiss();
                                            }
                                            if(receivedString.equals("one")){
                                                ShowPopupWindowOne();
                                            }
                                            if(receivedString.equals("two")){
                                                ShowPopupWindowTwo();
                                            }
                                            if(receivedString.equals("three")){
                                                ShowPopupWindowThree();
                                            }
                                            if(receivedString.equals("four")){
                                                ShowPopupWindowFour();
                                            }


                                        }
                                    });

                                }else{
                                    readBuffer[readBufferPosition++] = b;
                                }

                            }

                        }

                    } catch (IOException e) {
                        //e.printStackTrace();
                        stopWorker = true;
                    }
                }

            }
        });

        workerThread.start();
       // return receivedDataBT[1];

    }

    public String[] doLoad(String key){


        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        String str = sp.getString(key, "DEFAULT");

        String[] stringArray = str.split(":");

        return stringArray;


    }

    public void doDelete(String key){
        SharedPreferences sp =PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();

        editor.remove(key);
        editor.apply();
    }

}
