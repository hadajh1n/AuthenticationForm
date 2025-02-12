package com.example.authenticationform;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UserActivity extends AppCompatActivity {

    private TextView user_name, dbOutput;
    private DatabaseHelper dbHelper;
    private Cursor cursor;
    private Button button_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        user_name = findViewById(R.id.user_name);
        dbOutput = findViewById(R.id.dbOutput);
        button_delete = findViewById(R.id.button_delete);

        user_name.setText("Привет, пользователь =)");

        dbHelper = new DatabaseHelper(this);
        cursor = dbHelper.getAllUsers();

        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    StringBuilder stringBuilder = new StringBuilder();

                    do {
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

                    } while (cursor.moveToNext());

                    dbOutput.setText(stringBuilder.toString());
                } else {
                    dbOutput.setText("База данных пуста.");
                    Log.d("DB_OUTPUT", "База данных пуста.");
                }
            } finally {
                cursor.close();
            }
        } else {
            Log.e("DB_ERROR", "Ошибка при получении данных из БД");
        }

        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.deleteUser();
                overridePendingTransition(0,0);
                recreate();
                overridePendingTransition(0,0);
            }
        });
    }

    public void goBackMainActivity(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(0,0);
    }
}