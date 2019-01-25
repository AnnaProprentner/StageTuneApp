package com.example.anna.simplebluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

    EditText edtSend;
    EditText edtReceive;
    Button btnStart;
    Spinner spStringSelect;

    String message;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_tuning);

        edtSend = (EditText) findViewById(R.id.edtSend);
        edtReceive = (EditText) findViewById(R.id.edtReceive);
        btnStart = (Button) findViewById(R.id.btnStart);
        spStringSelect = (Spinner) findViewById(R.id.spStringSelect);

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
              message = edtSend.getText().toString();
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                SendString(message);
            }
        });

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
                                    readBufferPosition = 0;

                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            //edtMessage.setText(data);
                                            Toast.makeText(getApplicationContext(), data, Toast.LENGTH_SHORT).show();

                                            edtReceive.setText(data);

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

    }

}
