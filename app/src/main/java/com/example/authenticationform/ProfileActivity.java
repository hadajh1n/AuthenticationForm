package com.example.authenticationform;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TextView user_name = findViewById(R.id.user_name);

        Button back_user_activity = findViewById(R.id.back_user_activity);

        EditText profile_name = findViewById(R.id.profile_name);
        EditText profile_login = findViewById(R.id.profile_login);
        EditText profile_password = findViewById(R.id.profile_password);

        DatabaseHelper dbHelper = new DatabaseHelper(this);

        SharedPreferences preferences = getSharedPreferences("auth", MODE_PRIVATE);
        String userLogin = preferences.getString("USER_LOGIN", null);

        if (userLogin != null) {
            String name = dbHelper.user_name(userLogin);
            user_name.setText("Привет, " + name + "!");
        } else {
            user_name.setText("Гость");
        }

        back_user_activity.setOnClickListener(view -> {
            back_user_activity(view);
        });

        loadUserDataprofile(dbHelper, userLogin, profile_name, profile_login, profile_password);
    }

    private void loadUserDataprofile(DatabaseHelper dbHelper, String login, EditText profile_name, EditText profile_login, EditText profile_password) {
        Cursor cursor = dbHelper.getUserData(login);

        if (cursor != null && cursor.moveToFirst()) {
            String userName = cursor.getString(1);
            String userLogin = cursor.getString(2);
            String userPassword = cursor.getString(3);
            profile_name.setText(userName);
            profile_login.setText(userLogin);
            profile_password.setText(userPassword);
            cursor.close();
        }
    }

    private void back_user_activity(View view) {
        Intent intent = new Intent(this, UserActivity.class);
        startActivity(intent);
        overridePendingTransition(0,0);
    }
}