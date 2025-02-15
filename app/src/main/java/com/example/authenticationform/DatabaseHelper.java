package com.example.authenticationform;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "UserBD";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_LOGIN = "login";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_IS_ADMIN = "is_admin";

    public static final String LOGIN_ADMIN = "hadajh1n";
    private static final String PASSWORD_ADMIN = "12345";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_USERS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT NOT NULL, " +
                COLUMN_LOGIN + " TEXT NOT NULL UNIQUE, " +
                COLUMN_PASSWORD + " TEXT NOT NULL, " +
                COLUMN_IS_ADMIN + " INTEGER DEFAULT 0)";
        db.execSQL(CREATE_TABLE);

        addAdmin(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Cursor cursor = db.rawQuery("PRAGMA table_info(" + TABLE_USERS + ")", null);
        boolean columnExists = false;

        while(cursor.moveToNext()) {
            String columnName = cursor.getString(1);
            if (COLUMN_IS_ADMIN.equals(columnName)) {
                columnExists = true;
                break;
            }
        }
        cursor.close();

        if (!columnExists) {
            db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_IS_ADMIN + " INTEGER DEFAULT 0");
        }

        ensureAdminExists(db);
    }

    private void addAdmin(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, "Admin");
        values.put(COLUMN_LOGIN, LOGIN_ADMIN);
        values.put(COLUMN_PASSWORD, PASSWORD_ADMIN);
        values.put(COLUMN_IS_ADMIN, 1);

        try {
            db.insertOrThrow(TABLE_USERS, null, values);
        } catch (Exception e) {
            Log.e("DB_ERROR", "Администратор уже существует");
        }
    }

    private void ensureAdminExists(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_IS_ADMIN + "=1", null);
        if(cursor.getCount() == 0) {
            addAdmin(db);
        }
        cursor.close();
    }

    public boolean isAdmin(String login, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_ID},
                COLUMN_LOGIN + "=? AND " + COLUMN_PASSWORD + "=? AND " + COLUMN_IS_ADMIN + "=1",
                new String[]{login, password}, null, null, null);

        boolean isAdmin = cursor.getCount() > 0;
        cursor.close();
        return isAdmin;
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
        }
        return result;
    }

    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM users", null);
    }

    public Cursor getUserData(String login) {
        SQLiteDatabase db = this.getReadableDatabase();
         return db.rawQuery("SELECT * FROM users WHERE login =?", new String[]{login});
    }

    public void deleteUser() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(TABLE_USERS, COLUMN_IS_ADMIN + "=0", null);
            db.execSQL("DELETE FROM sqlite_sequence WHERE name=?", new String[]{TABLE_USERS});
        } catch (Exception e) {
            Log.e("DB_ERROR", "Ошибка при удалении пользователей: " + e.getMessage());
        }
    }

    public int authenticationUser(String login, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_ID},
                COLUMN_LOGIN + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{login,password},null,null,null);

        int userID = -1;
        if (cursor.moveToFirst()) {
            userID = cursor.getInt(0);
        }
        cursor.close();
        return userID;
    }

    public String user_name(String login) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_NAME},
                COLUMN_LOGIN + "=?", new String[]{login}, null,null,null);

        String name = "пользователь";
        if (cursor.moveToFirst()) {
            name = cursor.getString(0);
        }
        cursor.close();
        return name;
    }
}
