package com.example.anna.simplebluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    Button btnConnect, btnAlive;
    private static final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
    BluetoothDevice device = null;
    boolean found;

    BluetoothSocket btSocket = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnConnect = (Button)findViewById(R.id.btnConnect);
        btnAlive = (Button)findViewById(R.id.btnAlive);




        if (bluetoothAdapter == null) {

            Toast.makeText(getApplicationContext(),"Device doesnt Support Bluetooth",Toast.LENGTH_SHORT).show();

        }

        if (!bluetoothAdapter.isEnabled())
        {
            //Ask to the user turn the bluetooth on
            Intent enableAdapter  = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableAdapter ,1);
        }


        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectESP();
            }
        });

        btnAlive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendString("I AM ALIVE");
            }
        });


    }

    public void ConnectESP(){
        if (pairedDevices.isEmpty()) {

            Toast.makeText(getApplicationContext(), "Please Pair the Device first", Toast.LENGTH_SHORT).show();

        } else {

            for (BluetoothDevice iterator : pairedDevices) {

                if (iterator.getAddress().toString().equals("30:AE:A4:75:5B:1E"))

                {

                    device = iterator; //device is an object of type BluetoothDevice

                    found = true;

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
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    public void SendString(String s){
        try {
            btSocket.getOutputStream().write(s.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
