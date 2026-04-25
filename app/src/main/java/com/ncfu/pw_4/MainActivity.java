package com.ncfu.pw_4;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SQLHelper2 dbHelper2;
    private LinearLayout patientContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper2 = new SQLHelper2(this);
        patientContainer = findViewById(R.id.patientContainer);

        Button btnAddPatient = findViewById(R.id.btnAddPatient);
        Button btnShowPatients = findViewById(R.id.btnShowPatients);

        btnAddPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id = dbHelper2.addPatient(
                        "Ткачев Сергей Юрьевич",
                        "22.11.2002",
                        "1234567890",
                        "ОРВИ"
                );

                if (id != -1) {
                    Toast.makeText(MainActivity.this,
                            "Пациент добавлен с ID: " + id,
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this,
                            "Ошибка добавления пациента",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnShowPatients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAllPatients();
            }
        });
    }

    private void displayAllPatients() {
        ArrayList<Patient> patients = dbHelper2.getAllPatients();
        patientContainer.removeAllViews();

        if (patients.isEmpty()) {
            TextView emptyView = new TextView(this);
            emptyView.setText("Список пациентов пуст");
            patientContainer.addView(emptyView);
            return;
        }

        for (Patient patient : patients) {
            TextView textView = new TextView(this);
            textView.setText(
                    "Пациент #" + patient.getId()
                            + " | " + patient.getFullName()
                            + " | " + patient.getBirthDate()
                            + " | " + patient.getPolicyNumber()
                            + " | " + patient.getDiagnosis()
            );
            textView.setTextSize(16);
            textView.setPadding(8, 8, 8, 8);
            patientContainer.addView(textView);
        }
    }
}