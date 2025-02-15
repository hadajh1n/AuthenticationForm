package com.example.authenticationform;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button button_next;
    private DatabaseHelper dbHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        SharedPreferences preferences = getSharedPreferences("auth", MODE_PRIVATE);
        String savedLogin = preferences.getString("USER_LOGIN", "");

        if (!savedLogin.isEmpty()) {
            if (savedLogin.equals((DatabaseHelper.LOGIN_ADMIN))) {
                startActivity(new Intent(this, AdminActivity.class));
            } else {
                startActivity(new Intent(this, UserActivity.class));
            }
            finish();
        }

        button_next = findViewById(R.id.button_next);
        EditText editLogin = findViewById(R.id.login_auth);
        EditText editPassword = findViewById(R.id.password_auth);

        button_next.setOnClickListener(v -> {
            String login = editLogin.getText().toString().trim();
            String password = editPassword.getText().toString().trim();

            if(login.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Заполните поля", Toast.LENGTH_SHORT).show();
                return;
            }

            if(dbHelper.isAdmin(login, password)) {
                savedLogin(login);
                goAdmin();
            } else {
                loginUser(login, password);
            }
        });
    }

    private void savedLogin(String login) {
        SharedPreferences preferences = getSharedPreferences("auth", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("USER_LOGIN", login);
        editor.apply();
    }

    private void goAdmin() {
        Intent intent = new Intent(this, AdminActivity.class);
        startActivity(intent);
        overridePendingTransition(0,0);
        finish();
    }

    public void loginUser(String login, String password) {
        int userID = dbHelper.authenticationUser(login, password);

        if(userID != -1) {
            Toast.makeText(this, "Вход выполнен", Toast.LENGTH_SHORT).show();
            savedLogin(login);

            Intent intent = new Intent(this, UserActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
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