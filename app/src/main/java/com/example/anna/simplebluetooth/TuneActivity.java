package com.example.anna.simplebluetooth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class TuneActivity extends AppCompatActivity {

    Spinner spE,spA,spD,spG,spH,spe;
    Button btnStartTuning;

    String selectedE,selectedA,selectedD,selectedG,selectedH,selectede;

    String[] newTune = new String[7];

    int count = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tune);

        spE = (Spinner) findViewById(R.id.spStringE);
        spA = (Spinner) findViewById(R.id.spStringA);
        spD = (Spinner) findViewById(R.id.spStringD);
        spG = (Spinner) findViewById(R.id.spStringG);
        spH = (Spinner) findViewById(R.id.spStringH);
        spe = (Spinner) findViewById(R.id.spStringe);

        btnStartTuning = (Button) findViewById(R.id.btnStartTuning);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.string_names, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spE.setAdapter(adapter);
        spA.setAdapter(adapter);
        spD.setAdapter(adapter);
        spG.setAdapter(adapter);
        spH.setAdapter(adapter);
        spe.setAdapter(adapter);

        spE.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedE = spE.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedA = spA.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spD.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedD = spD.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spG.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedG = spG.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spH.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedH = spH.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectede = spe.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


            newTune[0] = "Tune" + count;

            newTune[1] = selectedE;
            newTune[2] = selectedA;
            newTune[3] = selectedD;
            newTune[4] = selectedG;
            newTune[5] = selectedH;
            newTune[6] = selectede;



        btnStartTuning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                count++;

                Toast.makeText(getApplicationContext(), selectedE, Toast.LENGTH_SHORT).show();

                Intent i3 = new Intent(TuneActivity.this,NowTuningActivity.class);
                startActivity(i3);

            }
        });

    }
}
