package de.nnordman.beaf;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import de.nnordman.beaf.Model.EventSqlLite;

public class DataBaseHelper extends SQLiteOpenHelper {
    //Database
    private static final int DATABASE_VER = 3;
    private static final String DATABASE_NAME = "BEAFSQL.db";

    //Table
    private static final String TABLE_NAME = "EventsSQL";
    private static final String KEY_ID = "ID";
    private static final String KEY_NAME = "EventName";
    private static final String KEY_LOCATION = "Location";
    private static final String KEY_DATE = "Date";
    private static final String KEY_TIME = "Time";
    //private static final String KEY_PERSON = "Person";
    private static final String KEY_NOTES = "Notes";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + KEY_NAME + " TEXT ,"
                + KEY_LOCATION + " TEXT," + KEY_DATE + " TEXT," + KEY_TIME + " TEXT," + KEY_NOTES + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    //CRUD Events
    public void addEvent(EventSqlLite eventSqlLite) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, eventSqlLite.getEventNameSql());
        values.put(KEY_LOCATION, eventSqlLite.getLocationSql());
        values.put(KEY_DATE, eventSqlLite.getDateSql());
        values.put(KEY_TIME, eventSqlLite.getTimeSql());
        //values.put(KEY_PERSON, String.valueOf(eventSqlLite.getPersonSql()));
        values.put(KEY_NOTES, eventSqlLite.getNotesSql());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public int updateEvent(EventSqlLite eventSqlLite) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, eventSqlLite.getEventNameSql());
        values.put(KEY_LOCATION, eventSqlLite.getLocationSql());
        values.put(KEY_DATE, eventSqlLite.getDateSql());
        values.put(KEY_TIME, eventSqlLite.getTimeSql());
        //values.put(KEY_PERSON, String.valueOf(eventSqlLite.getPersonSql()));
        values.put(KEY_NOTES, eventSqlLite.getNotesSql());

        return db.update(TABLE_NAME, values, KEY_ID + " =?", new String[]{String.valueOf(eventSqlLite.getId())});
    }

    public Integer deleteEvent(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?", new String[]{id});

/*        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " =?", new String[]{String.valueOf(eventSqlLite.getId())});
        db.close();*/
    }

    public EventSqlLite getEvent(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{KEY_ID, KEY_NAME, KEY_LOCATION, KEY_DATE, KEY_TIME, KEY_NOTES}
                , KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return new EventSqlLite(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                cursor.getString(3), cursor.getString(4), cursor.getString(5));
    }

    public List<EventSqlLite> getAllEvents() {
        List<EventSqlLite> listEvents = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                EventSqlLite eventSqlLite = new EventSqlLite();
                eventSqlLite.setEventNameSql(cursor.getString(0));
                eventSqlLite.setLocationSql(cursor.getString(1));
                eventSqlLite.setDateSql(cursor.getString(2));
                eventSqlLite.setTimeSql(cursor.getString(3));
                eventSqlLite.setNotesSql(cursor.getString(5));

                listEvents.add(eventSqlLite);
            }
            while (cursor.moveToFirst());
        }
        return listEvents;
    }

    public Cursor getAllEventsData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return res;
    }

    public Cursor getAllSearch(String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_DATE + " =?", new String[]{date});
        return res;
    }

    public Cursor sortDate() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor sort = db.query(TABLE_NAME, null, null, null, null, null, KEY_DATE + " ASC");
        return sort;
    }
}
