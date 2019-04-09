package com.example.anna.simplebluetooth;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnFreeTune, btnSavedTunes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnFreeTune = (Button) findViewById(R.id.btnFreeTune);
        btnSavedTunes = (Button) findViewById(R.id.btnSavedTunes);


        btnFreeTune.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
    }
}
