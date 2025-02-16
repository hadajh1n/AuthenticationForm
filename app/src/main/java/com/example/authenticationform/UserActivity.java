package com.example.authenticationform;

import com.example.authenticationform.R;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.RouteListingPreference;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        TextView user_name = findViewById(R.id.user_name);
        TextView dbOutput = findViewById(R.id.dbOutput);

        Button menuButton = findViewById(R.id.button_menu);
        menuButton.setOnClickListener(this::showMenu);

        DatabaseHelper dbHelper = new DatabaseHelper(this);

        SharedPreferences preferences = getSharedPreferences("auth", MODE_PRIVATE);
        String userLogin = preferences.getString("USER_LOGIN", null);

        if (userLogin == null || userLogin.isEmpty()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        if (userLogin != null) {
            String name = dbHelper.user_name(userLogin);
            user_name.setText("Привет, " + name + "!");
        } else {
            user_name.setText("Гость");
        }

        loadUserData(dbHelper, userLogin, dbOutput);
    }

    private void loadUserData(DatabaseHelper dbHelper, String login, TextView dbOutput) {
        Cursor cursor = dbHelper.getUserData(login);

        if (cursor != null && cursor.moveToFirst()) {
            String userInfo = "Имя: " + cursor.getString(1) +
                    "\nЛогин: " + cursor.getString(2) +
                    "\nПароль: " + cursor.getString(3);
            dbOutput.setText(userInfo);
            cursor.close();
        } else {
            dbOutput.setText("Ошибка загрузки данных...");
        }
    }

    private void showMenu(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {
            handleClick(item);
            return true;
        });

        popup.show();
    }

    private void handleClick(MenuItem item) {
        int itemID = item.getItemId();

        if (itemID == R.id.action_profile) {

        } else if (itemID == R.id.action_settings) {

        } else if (itemID == R.id.action_logout) {
            logout();
        }
    }

    private void logout() {
        SharedPreferences preferences = getSharedPreferences("auth", MODE_PRIVATE);
        preferences.edit().clear().apply();

        Intent intent = new Intent(UserActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }
}