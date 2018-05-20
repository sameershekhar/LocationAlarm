package com.example.sameershekhar.locationalarm.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sameershekhar on 12-Mar-18.
 */

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "alarmManager";

    // Contacts table name
    private static final String TABLE_USERS = "alarm";

    // Contacts Table Columns names
    private static final String KEY_ID="id";
    private static final String LOCATION = "location";
    private static final String DESCRIPTION = "description";
    private static final String TIME = "time";
    private static final String LATLONG="latlong";
    private static final String MILLISECOND="milli";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + KEY_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + LOCATION + " TEXT," + DESCRIPTION + " TEXT,"+ MILLISECOND + " TEXT,"
                + TIME + " TEXT" + ")";
        db.execSQL(CREATE_USERS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);

        onCreate(db);
    }

    public void addAlarm(Alarm alarm) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LOCATION, alarm.getLocation());
        values.put(DESCRIPTION, alarm.getDescription());
        values.put(MILLISECOND,alarm.getMilliSecond());
        values.put(TIME,alarm.getDateTime());

        //values.put(LATLONG,alarm.getLatLng().toString());


        db.insertWithOnConflict(TABLE_USERS, null, values,SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public List<Alarm> getAllAlarms() {
        List<Alarm> usersList = new ArrayList<Alarm>();

        String selectQuery = "SELECT  * FROM " + TABLE_USERS + " ORDER BY "+  MILLISECOND +" DESC";


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Alarm users = new Alarm();
                //users.setLatLng();
                users.setLocation(cursor.getString(1));
                users.setDescription(cursor.getString(2));
                users.setMilliSecond(cursor.getLong(3));
                users.setDateTime(cursor.getString(4));
                usersList.add(users);
            } while (cursor.moveToNext());
        }


        return usersList;
    }
    public int getAlarmsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_USERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }
    public void deleteAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE  FROM "+ TABLE_USERS);
        db.close();
    }

    public void deleteContact(String place) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS, LOCATION + " = ?",
                new String[] { place });
        db.close();
    }

}

