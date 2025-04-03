package com.example.myappp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Проверка, зарегистрирован ли пользователь
        if (!isUserRegistered()) {
            // Если пользователь не зарегистрирован, перенаправляем на RegistrationActivity
            Intent intent = new Intent(this, RegistrationActivity.class);
            startActivity(intent);
            finish(); // Закрываем MainActivity
            return;
        }
        // Если пользователь зарегистрирован, продолжаем загрузку MainActivity
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Загрузка имени пользователя из SharedPreferences
        String realName = loadUserName();

        // Находим TextView и устанавливаем приветствие
        TextView textViewGreeting = findViewById(R.id.textView_greeting);
        textViewGreeting.setText("Привет, " + realName + "!");

        findViewById(R.id.button_open_habit_list_activity).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HabitListActivity.class);
            startActivity(intent);
        });
    }

    /**Метод для проверки, зарегистрирован ли пользователь*/
    private boolean isUserRegistered() {
        SharedPreferences preferences = getSharedPreferences("UserData", MODE_PRIVATE);
        return preferences.contains("username");
    }

    /**Метод для загрузки имени пользователя из SharedPreferences*/
    private String loadUserName() {
        SharedPreferences preferences = getSharedPreferences("UserData", MODE_PRIVATE);
        return preferences.getString("username", "Гость"); // "Гость" — значение по умолчанию
    }
}