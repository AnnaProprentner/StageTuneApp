package com.example.anna.simplebluetooth;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class NewTuneActivity extends AppCompatActivity {

    Spinner spE, spA, spD, spG, spH, spe;
    Spinner spOE, spOA, spOD, spOG, spOH, spOe;
    Button btnStartTuning, btnSave;

    //Dialog
    EditText edtKey;
    Dialog dialogSave;
    Button btnDialogSave;

    //Error
    Dialog dialogErr;
    TextView txtErr;
    Button btnOk;
    boolean wrongNote = false;

    String selectedNoteE, selectedNoteA, selectedNoteD, selectedNoteG, selectedNoteH, selectedNotee;
    String selectedOctaveE, selectedOctaveA, selectedOctaveD, selectedOctaveG, selectedOctaveH, selectedOctavee;

    String tuneKey;

    String[] newTune = new String[6];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tune);

        spE = (Spinner) findViewById(R.id.spStringE);
        spOE = (Spinner) findViewById(R.id.spOctaveE);

        spA = (Spinner) findViewById(R.id.spStringA);
        spOA = (Spinner) findViewById(R.id.spOctaveA);

        spD = (Spinner) findViewById(R.id.spStringD);
        spOD = (Spinner) findViewById(R.id.spOctaveD);

        spG = (Spinner) findViewById(R.id.spStringG);
        spOG = (Spinner) findViewById(R.id.spOctaveG);

        spH = (Spinner) findViewById(R.id.spStringH);
        spOH = (Spinner) findViewById(R.id.spOctaveH);

        spe = (Spinner) findViewById(R.id.spStringe);
        spOe = (Spinner) findViewById(R.id.spOctavee);


        btnStartTuning = (Button) findViewById(R.id.btnStartTuning);
        btnSave = (Button) findViewById(R.id.btnSave);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.notes, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> adapterOct = ArrayAdapter.createFromResource(this,
                R.array.octave, android.R.layout.simple_spinner_item);
        adapterOct.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spE.setAdapter(adapter);
        spOE.setAdapter(adapterOct);

        spA.setAdapter(adapter);
        spOA.setAdapter(adapterOct);

        spD.setAdapter(adapter);
        spOD.setAdapter(adapterOct);

        spG.setAdapter(adapter);
        spOG.setAdapter(adapterOct);

        spH.setAdapter(adapter);
        spOH.setAdapter(adapterOct);

        spe.setAdapter(adapter);
        spOe.setAdapter(adapterOct);

        spE.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedNoteE = spE.getItemAtPosition(i).toString();
                //Toast.makeText(getApplicationContext(), selectedNoteE, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spOE.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedOctaveE = spOE.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedNoteA = spA.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spOA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedOctaveA = spOA.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spD.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedNoteD = spD.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spOD.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedOctaveD = spOD.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spG.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedNoteG = spG.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spOG.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedOctaveG = spOG.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spH.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedNoteH = spH.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spOH.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedOctaveH = spOH.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedNotee = spe.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spOe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedOctavee = spOe.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getStrings();

                if (!wrongNote) {
                    dialogSave = new Dialog(NewTuneActivity.this);
                    dialogSave.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialogSave.setContentView(R.layout.dialog_tune_name);
                    dialogSave.show();

                    edtKey = dialogSave.findViewById(R.id.edtKey);
                    btnDialogSave = dialogSave.findViewById(R.id.btnDialogSave);


                    btnDialogSave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            tuneKey = edtKey.getText().toString().trim();


                            doSave(tuneKey, newTune);

                            dialogSave.dismiss();

                            Intent i4 = new Intent(NewTuneActivity.this, SavedTunesActivity.class);
                            startActivity(i4);

                        }
                    });
                } else if (wrongNote) {
                    dialogErr = new Dialog(NewTuneActivity.this);
                    dialogErr.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialogErr.setContentView(R.layout.dialog_err_frequencyrange);

                    txtErr = dialogErr.findViewById(R.id.txtError);
                    txtErr.setText(R.string.frequencyBorder);

                    btnOk = (Button) dialogErr.findViewById(R.id.btnOk);

                    btnOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialogErr.dismiss();
                        }
                    });

                    dialogErr.show();
                }
            }
        });


        btnStartTuning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getStrings();

                if (!wrongNote) {

                    Intent i3 = new Intent(NewTuneActivity.this, NowTuningActivity.class);
                    i3.putExtra("Tune", newTune);

                    startActivity(i3);
                } else if (wrongNote) {
                    dialogErr = new Dialog(NewTuneActivity.this);
                    dialogErr.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialogErr.setContentView(R.layout.dialog_err_frequencyrange);

                    txtErr = dialogErr.findViewById(R.id.txtError);
                    txtErr.setText(R.string.frequencyBorder);

                    btnOk = (Button) dialogErr.findViewById(R.id.btnOk);

                    btnOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialogErr.dismiss();
                        }
                    });

                    dialogErr.show();
                }
            }
        });
    }

    public void getStrings() {

        newTune[0] = selectedNoteE + selectedOctaveE;
        newTune[1] = selectedNoteA + selectedOctaveA;
        newTune[2] = selectedNoteD + selectedOctaveD;
        newTune[3] = selectedNoteG + selectedOctaveG;
        newTune[4] = selectedNoteH + selectedOctaveH;
        newTune[5] = selectedNotee + selectedOctavee;

        for (int i = 0; i < 6; i++) {
            if (newTune[i].equals("H4") | newTune[i].equals("A4") | newTune[i].equals("G4") | newTune[i].equals("F4") | newTune[i].equals("D2") | newTune[i].equals("C2")) {
                wrongNote = true;
            } else {
                wrongNote = false;
            }
        }
    }

    public void doSave(String key, String[] stringArray) {
        String tuneString = "";

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < stringArray.length; i++) {
            sb.append(stringArray[i]).append(" ");
        }
        tuneString = sb.toString();

        editor.putString(key, tuneString);
        editor.apply();
    }
}

