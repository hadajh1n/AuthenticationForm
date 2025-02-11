package com.example.authenticationform;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegistrationActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private EditText nameInput, loginInput, passwordInput;
    private Button button_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        dbHelper = new DatabaseHelper(this);

        nameInput = findViewById(R.id.nameInput);
        loginInput = findViewById(R.id.loginInput);
        passwordInput = findViewById(R.id.passwordInput);
        button_save = findViewById(R.id.button_save);

        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameInput.getText().toString();
                String login = loginInput.getText().toString();
                String password = passwordInput.getText().toString();

                if (name.isEmpty() || login.isEmpty() || password.isEmpty()) {
                    Toast.makeText(RegistrationActivity.this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                    return;
                }

                long result = dbHelper.addUser(name, login, password);
                if(result != -1) {
                    Toast.makeText(RegistrationActivity.this, "Пользователь сохранен", Toast.LENGTH_SHORT).show();
                    goUser(v);
                } else {
                    Toast.makeText(RegistrationActivity.this, "Ошибка сохранения", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void goBack(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(0,0);
    }

    public void goUser(View v) {
        Intent intent = new Intent(this, UserActivity.class);
        startActivity(intent);
    }
}