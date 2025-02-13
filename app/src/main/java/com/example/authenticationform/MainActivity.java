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

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = getSharedPreferences("auth", MODE_PRIVATE);
        String savedLogin = preferences.getString("USER_LOGIN", null);

        if (savedLogin != null) {
            Intent intent = new Intent(this, UserActivity.class);
            startActivity(intent);
            overridePendingTransition(0,0);
            finish();
        }

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
        int userID = dbHelper.authenticationUser(login, password);

        if (userID != -1) {
            Toast.makeText(this, "Вход выполнен", Toast.LENGTH_SHORT).show();

            SharedPreferences preferences = getSharedPreferences("auth", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("USER_LOGIN", login);
            editor.apply();

            Intent intent = new Intent(this, UserActivity.class);
            intent.putExtra("USER_LOGIN", login);
            startActivity(intent);
            overridePendingTransition(0,0);
            finish();
        } else {
            Toast.makeText(this, "Неверный логин или пароль", Toast.LENGTH_SHORT).show();
        }
    }

    /*
    public void loginAdmin(View view) {
        EditText loginInput = findViewById(R.id.login_auth);
        EditText passwordInput = findViewById(R.id.password_auth);

        String login = loginInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        int userID = dbHelper.authenticationUser(login,password);

        if (userID != -1) {
            Toast.makeText(this, "Вход выполнен!", Toast.LENGTH_SHORT).show();

            SharedPreferences preferences = getSharedPreferences("users", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("id", userID);
            editor.apply();

            Intent intent = new Intent(this, AdminActivity.class);
            intent.putExtra("id", userID);
            startActivity(intent);
            overridePendingTransition(0,0);
            finish();
        } else {
            Toast.makeText(this, "Неверный логин или пароль", Toast.LENGTH_SHORT).show();
        }
    }

    */

    public void registrationActivity(View v) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
        overridePendingTransition(0,0);
    }
}