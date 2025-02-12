package com.example.authenticationform;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private Button button_next;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_next = findViewById(R.id.button_next);

        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser(v);
            }
        });
    }

    public void loginUser(View view) {
        EditText loginInput = findViewById(R.id.login_auth);
        EditText passwordInput = findViewById(R.id.password_auth);

        String login = loginInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        DatabaseHelper dbHelper = new DatabaseHelper(this);

        if (dbHelper.authenticationUser(login, password)) {
            Toast.makeText(this, "Вход выполнен!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, UserActivity.class);
            startActivity(intent);
            overridePendingTransition(0,0);
            finish();
        } else {
            Toast.makeText(this, "Неверный логин или пароль", Toast.LENGTH_SHORT).show();
        }
    }
    public void registrationActivity(View v) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
        overridePendingTransition(0,0);
    }
}