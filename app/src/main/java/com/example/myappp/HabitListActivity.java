package com.example.myappp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import db.Habit;
import db.HabitDao;
import db.HabitDataBase;

public class HabitListActivity extends AppCompatActivity {

    private LinearLayout habitListContainer;
    private HabitDataBase habitDatabase;
    private HabitDao habitDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_habit_list);
        habitDatabase = HabitDataBase.getInstance(this);
        habitDao = habitDatabase.habitDao();

        habitListContainer = findViewById(R.id.habit_list_container);

        ImageButton calendarButton = findViewById(R.id.calendar_button);

        calendarButton.setOnClickListener(v -> {
            Intent intent = new Intent(HabitListActivity.this, CalendarActivity.class);
            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        loadHabits();
    }

    public void plusClick(View v){
        newHabit();
    }
    private void newHabit(){
        final EditText input = new EditText(HabitListActivity.this);
        AlertDialog.Builder builder = new AlertDialog.Builder(HabitListActivity.this);
        builder.setTitle("Добавление привычки")
                .setMessage("Введите привычку которую хотите добавить")
                .setCancelable(false)
                .setView(input)
                .setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String habitText = input.getText().toString().trim();
                        if (!habitText.isEmpty()) {
                            Habit newHabit = new Habit(habitText);
                            new Thread(() -> {
                                habitDao.insertHabit(newHabit);
                                runOnUiThread(() -> addHabitToScreen(habitText)); // Обновление экрана
                            }).start();
                        }
                    }
                })
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog dialog=builder.create();
        dialog.show();
    }

    private void addHabitToScreen(String habitText) {
        LayoutInflater inflater = LayoutInflater.from(this);

        View habitView = inflater.inflate(R.layout.habit_item, habitListContainer, false);

        TextView habitTextView = habitView.findViewById(R.id.habit_text);

        habitTextView.setText(habitText);

        habitListContainer.addView(habitView);
    }
    private void loadHabits() {
        new Thread(() -> {
            List<Habit> habits = habitDao.getAllHabits();
            runOnUiThread(() -> {
                for (Habit habit : habits) {
                    addHabitToScreen(habit.getName());
                }
            });
        }).start();
    }

}