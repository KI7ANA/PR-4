package com.ncfu.pw_4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class SQLHelper2 extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "clinic.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "patients";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_FULL_NAME = "full_name";
    public static final String COLUMN_BIRTH_DATE = "birth_date";
    public static final String COLUMN_POLICY = "policy_number";
    public static final String COLUMN_DIAGNOSIS = "diagnosis";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_FULL_NAME + " TEXT NOT NULL, "
            + COLUMN_BIRTH_DATE + " TEXT NOT NULL, "
            + COLUMN_POLICY + " TEXT NOT NULL, "
            + COLUMN_DIAGNOSIS + " TEXT NOT NULL);";

    public SQLHelper2(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long addPatient(String fullName, String birthDate, String policyNumber, String diagnosis) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FULL_NAME, fullName);
        values.put(COLUMN_BIRTH_DATE, birthDate);
        values.put(COLUMN_POLICY, policyNumber);
        values.put(COLUMN_DIAGNOSIS, diagnosis);
        long id = db.insert(TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public ArrayList<Patient> getAllPatients() {
        ArrayList<Patient> patientList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String fullName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FULL_NAME));
                String birthDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BIRTH_DATE));
                String policy = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_POLICY));
                String diagnosis = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIAGNOSIS));

                patientList.add(new Patient(id, fullName, birthDate, policy, diagnosis));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return patientList;
    }

    public int updatePatient(Patient patient) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FULL_NAME, patient.getFullName());
        values.put(COLUMN_BIRTH_DATE, patient.getBirthDate());
        values.put(COLUMN_POLICY, patient.getPolicyNumber());
        values.put(COLUMN_DIAGNOSIS, patient.getDiagnosis());

        return db.update(TABLE_NAME, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(patient.getId())});
    }

    public void deletePatient(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }
}