package com.example.authenticationform;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "UserBD";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_LOGIN = "login";
    private static final String COLUMN_PASSWORD = "password";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_USERS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT NOT NULL, " +
                COLUMN_LOGIN + " TEXT NOT NULL UNIQUE, " +
                COLUMN_PASSWORD + " TEXT NOT NULL)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public long addUser(String name, String login, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_LOGIN, login);
        values.put(COLUMN_PASSWORD, password);

        long result = -1;
        try {
            result = db.insertOrThrow(TABLE_USERS, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return result;
    }

    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USERS, null);
    }

    public void deleteUser() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS, null, null);
        db.execSQL("DELETE FROM sqlite_sequence WHERE name='" + TABLE_USERS + "'");
        db.close();
    }

    public boolean authenticationUser(String login, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " +
                COLUMN_LOGIN + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{login,password});

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }
}
