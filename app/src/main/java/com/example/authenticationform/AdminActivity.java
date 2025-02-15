package com.example.authenticationform;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SyncRequest;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AdminActivity extends AppCompatActivity {

    private TextView admin_name, dbOutput;
    private DatabaseHelper dbHelper;
    private Button button_delete;
    private Cursor cursor;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        admin_name = findViewById(R.id.admin_name);
        dbOutput = findViewById(R.id.dbOutput);
        button_delete = findViewById(R.id.button_delete);

        SharedPreferences preferences = getSharedPreferences("auth", MODE_PRIVATE);
        String userLogin = preferences.getString("USER_LOGIN", "");

        if (userLogin.equals(DatabaseHelper.LOGIN_ADMIN)) {
            admin_name.setText("Вы вошли как администратор");
            dbHelper = new DatabaseHelper(this);
            loadDatabaseData();
        } else {
            admin_name.setText("Ошибка входа");
            startActivity(new Intent(this, MainActivity.class));
            overridePendingTransition(0,0);
            finish();
            return;
        }

        dbHelper = new DatabaseHelper(this);
        loadDatabaseData();

        button_delete.setOnClickListener(view -> {
                dbHelper.deleteUser();
                loadDatabaseData();
            });
        }

        private void loadDatabaseData() {
            cursor = dbHelper.getAllUsers();

            if (cursor == null || cursor.getCount() == 0) {
                dbOutput.setText("База данных пуста.");
                Log.d("DB_OUTPUT", "База данных пуста.");
                return;
            }
            try {
                StringBuilder stringBuilder = new StringBuilder();
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(0);
                    String name = cursor.getString(1);
                    String login = cursor.getString(2);
                    String password = cursor.getString(3);

                    stringBuilder.append("ID: ").append(id)
                            .append("\nИмя: ").append(name)
                            .append("\nЛогин: ").append(login)
                            .append("\nПароль: ").append(password)
                            .append("\n-------------------------\n");

                    Log.d("DB_OUTPUT", "ID: " + id + ", Name: " + name + ", Login: " + login + ", Password: " + password);
                }
                dbOutput.setText(stringBuilder.toString());
            } catch (Exception e) {
                Log.e("DB_ERROR", "Ошибка при обработке данных: " + e.getMessage());
            } finally {
                cursor.close();
            }
        }

    public void goBackMainActivity(View v) {
        SharedPreferences preferences = getSharedPreferences("auth", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(0,0);
        finish();
    }
}