package com.example.anna.simplebluetooth;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.UUID;

public class NowTuningActivity extends AppCompatActivity {

    private static final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    BluetoothAdapter bluetoothAdapter;
    Set<BluetoothDevice> pairedDevices;

    BluetoothDevice device = null;
    boolean found = false;
    boolean connected = false;
    boolean bluetoothEnabled;

    public BluetoothSocket btSocket = null;

    public InputStream myInputStream;

    Thread workerThread;

    volatile boolean stopThread;
    byte[] readBuffer;
    int readBufferPosition;

    Button btnStart;
    Spinner spStringSelect;

    String selectedString;
    String receivedString = "";

    String[] Tune = new String[6];

    String message;

    String str = "";
    String s = "";

    //Popup Window
    Dialog epicDialog;
    ImageView imgClosePopupTuning;
    Button btnReadyPopup;
    TextView txtStringToTune;


    //EnableBluetooth
    Dialog requestDialog;
    Button btnAllow;
    Button btnCancel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_tuning);

        if (getIntent().hasExtra("Tune") == true) {
            Tune = getIntent().getExtras().getStringArray("Tune");
        }

        btnStart = (Button) findViewById(R.id.btnStart);
        spStringSelect = (Spinner) findViewById(R.id.spStringSelect);

        epicDialog = new Dialog(this);
        requestDialog = new Dialog(this);


        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), R.string.noSupport, Toast.LENGTH_SHORT).show();
        }

        if (!bluetoothAdapter.isEnabled()) {
            showBluetoothEnable();
        }

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.string_names, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spStringSelect.setAdapter(adapter);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (bluetoothAdapter.isEnabled()) {

                    ConnectESP();
                } else {
                    showBluetoothEnable();
                    return;
                }

                if (connected) {
                    listenForData();
                } else {
                    return;
                }

                // String you want to tune
                selectedString = spStringSelect.getSelectedItem().toString();


                if (selectedString.equals("E")) {
                    s = Tune[0];

                } else if (selectedString.equals("A")) {
                    s = Tune[1];

                } else if (selectedString.equals("D")) {
                    s = Tune[2];

                } else if (selectedString.equals("G")) {
                    s = Tune[3];

                } else if (selectedString.equals("H")) {
                    s = Tune[4];

                } else if (selectedString.equals("e")) {
                    s = Tune[5];

                }

                SendString(convertToFrequency(s)); //sends required frequency as string

                ShowPopupWindowOne();
            }
        });

    }


    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (connected) {
                btSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showBluetoothEnable() {
        //requestDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        requestDialog.setContentView(R.layout.dialog_enable_bluetooth);

        btnAllow = (Button) requestDialog.findViewById(R.id.btnAllow);
        btnCancel = (Button) requestDialog.findViewById(R.id.btnCancel);

        btnAllow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BluetoothAdapter.getDefaultAdapter().enable();

                bluetoothEnabled = true;
                requestDialog.dismiss();
                requestDialog.cancel();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestDialog.dismiss();
                requestDialog.cancel();
            }
        });

        requestDialog.setCanceledOnTouchOutside(false);
        requestDialog.show();
    }

    public void ShowPopupWindowOne() {
        epicDialog.setContentView(R.layout.dialog_tuning_one);
        btnReadyPopup = (Button) epicDialog.findViewById(R.id.btnReadyPopUp);
        txtStringToTune = (TextView) epicDialog.findViewById(R.id.txtStringToTune);

        txtStringToTune.setText(selectedString);

        btnReadyPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowPopupWindowTwo();
            }
        });

        epicDialog.setCanceledOnTouchOutside(false);
        epicDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        epicDialog.show();
    }

    public void ShowPopupWindowTwo() {
        epicDialog.setContentView(R.layout.dialog_tuning_two);

        txtStringToTune = (TextView) epicDialog.findViewById(R.id.txtStringToTune);
        txtStringToTune.setText(selectedString);

        epicDialog.setCanceledOnTouchOutside(false);

        epicDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        epicDialog.show();
    }

    public void ShowPopupWindowThree() {
        epicDialog.setContentView(R.layout.dialog_tuning_three);

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

    public void ShowPopupWindowFour() {
        epicDialog.setContentView(R.layout.dialog_tuning_four);

        txtStringToTune = (TextView) epicDialog.findViewById(R.id.txtStringToTune);

        txtStringToTune.setText(selectedString);

        epicDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        epicDialog.show();
    }

    public void ConnectESP() {

        pairedDevices = bluetoothAdapter.getBondedDevices();

        if (pairedDevices.isEmpty()) {
            Toast.makeText(getApplicationContext(),R.string.pairDevice , Toast.LENGTH_SHORT).show();
        } else {
            for (BluetoothDevice iterator : pairedDevices) {
                if (iterator.getAddress().equals("30:AE:A4:75:5B:1E")) {
                    device = iterator;
                    found = true;
                    break;
                }
            }
            if (!found) {
                Toast.makeText(getApplicationContext(),R.string.notFound, Toast.LENGTH_LONG).show();
                pairedDevices = null;
                return;
            }
        }

        if (device != null && !connected) {
            try {
                btSocket = device.createRfcommSocketToServiceRecord(PORT_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                btSocket.connect();
                connected = true;
                Toast.makeText(getApplicationContext(), R.string.connected, Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                connected = false;
                Toast.makeText(getApplicationContext(), R.string.connectionFailed, Toast.LENGTH_LONG).show();
                btSocket = null;
                return;
            }
            try {
                myInputStream = btSocket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void SendString(String s) {
        try {
            btSocket.getOutputStream().write(s.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listenForData() {
        final Handler handler = new Handler();
        final byte delimiter = 59; //Delimiter in ASCII code == ';'

        stopThread = false;
        readBufferPosition = 0;
        readBuffer = new byte[1024];

        workerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted() && !stopThread) {

                    try {
                        int bytesAvailable = myInputStream.available();
                        if (bytesAvailable > 0) {
                            byte[] packetBytes = new byte[bytesAvailable];
                            myInputStream.read(packetBytes);

                            for (int i = 0; i < bytesAvailable; i++) {

                                byte b = packetBytes[i];

                                if (b == delimiter) {
                                    byte[] encodedBytes = new byte[readBufferPosition];

                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes, "US-ASCII");

                                    receivedString = data;
                                    readBufferPosition = 0;

                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            receivedString = data.trim();
                                            //Toast.makeText(getApplicationContext(), receivedString, Toast.LENGTH_SHORT).show();

                                            if (receivedString.equals("close")) {
                                                epicDialog.dismiss();
                                            }
                                            if (receivedString.equals("one")) {
                                                ShowPopupWindowOne();
                                            }
                                            if (receivedString.equals("two")) {
                                                ShowPopupWindowTwo();
                                            }
                                            if (receivedString.equals("three")) {
                                                ShowPopupWindowThree();
                                            }
                                            if (receivedString.equals("four")) {
                                                ShowPopupWindowFour();
                                                //btSocket.close();
                                            }
                                        }
                                    });

                                } else {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }
                    } catch (IOException e) {
                        stopThread = true;
                    }
                }
            }
        });
        workerThread.start();
    }

    public String convertToFrequency(String string) {
        String ret = "";

        if (string.equals("E2")) {
            ret = "82.64;";
        } else if (string.equals("F2")) {
            ret = "92.50;";
        } else if (string.equals("G2")) {
            ret = "98.00;";
        } else if (string.equals("A2")) {
            ret = "110.00;";
        } else if (string.equals("H2")) {
            ret = "123.47;";
        } else if (string.equals("C3")) {
            ret = "130.81;";
        } else if (string.equals("D3")) {
            ret = "146.83;";
        } else if (string.equals("E3")) {
            ret = "164.81;";
        } else if (string.equals("F3")) {
            ret = "174,61;";
        } else if (string.equals("G3")) {
            ret = "196.00;";
        } else if (string.equals("A3")) {
            ret = "220.00;";
        } else if (string.equals("H3")) {
            ret = "246.94;";
        } else if (string.equals("C4")) {
            ret = "261.63;";
        } else if (string.equals("D4")) {
            ret = "293.67;";
        } else if (string.equals("E4")) {
            ret = "329.63;";
        }
        return ret;
    }

}
