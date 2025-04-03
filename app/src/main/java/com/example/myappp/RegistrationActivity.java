package com.example.myappp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegistrationActivity extends AppCompatActivity {

    private EditText editTextUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        editTextUsername = findViewById(R.id.editText_username);
        Button buttonRegister = findViewById(R.id.button_register);

        // Установка обработчика нажатия на кнопку регистрации
        buttonRegister.setOnClickListener(v -> {
            String username = editTextUsername.getText().toString();

            if (username.isEmpty()) {
                Toast.makeText(RegistrationActivity.this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            } else {
                saveUserCredentials(username);

                // Переход к MainActivity после успешной регистрации
                Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Закрытие текущей активности
            }
        });
    }

    /** Метод для сохранения данных пользователя в SharedPreferences*/
    private void saveUserCredentials(String username) {
        SharedPreferences preferences = getSharedPreferences("UserData", MODE_PRIVATE);
        preferences.edit()
                .putString("username", username)
                .apply();
    }
}