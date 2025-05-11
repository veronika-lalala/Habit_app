package activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myappp.R;

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

    public void plusClick(View v) {
        newHabit();
    }

    private void newHabit() {
        final EditText input = new EditText(HabitListActivity.this);
        AlertDialog.Builder builder = new AlertDialog.Builder(HabitListActivity.this);
        builder.setTitle("Добавление привычки")
                .setMessage("Введите привычку которую хотите добавить")
                .setCancelable(false)
                .setView(input)
                .setPositiveButton("Добавить", (dialog, which) -> {
                    String habitText = input.getText().toString().trim();
                    if (!habitText.isEmpty()) {
                        Habit newHabit = new Habit(habitText);
                        new Thread(() -> {
                            long id = habitDao.insertHabit(newHabit);
                            newHabit.setId(id);
                            runOnUiThread(() -> addHabitToScreen(newHabit));
                        }).start();
                    }
                })
                .setNegativeButton("Отмена", (dialog, which) -> dialog.cancel());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void addHabitToScreen(Habit habit) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View habitView = inflater.inflate(R.layout.habit_item, habitListContainer, false);

        TextView habitTextView = habitView.findViewById(R.id.habit_text);
        CheckBox habitCheckbox = habitView.findViewById(R.id.habit_checkbox);
        ImageButton actionsButton = habitView.findViewById(R.id.actions_button);

        habitTextView.setText(habit.getName());
        habitCheckbox.setChecked(habit.getIs_completed());

        // Обработчик для чекбокса
        habitCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            habit.setIs_completed(isChecked);
            new Thread(() -> habitDao.updateHabit(habit)).start();
        });

        // Обработчик для кнопки действий
        actionsButton.setOnClickListener(v -> {
            showActionsMenu(v, habit, habitView);
        });

        habitListContainer.addView(habitView);
    }

    private void showActionsMenu(View anchor, Habit habit, View habitView) {
        PopupMenu popup = new PopupMenu(this, anchor);
        popup.inflate(R.menu.habit_actions_menu);

        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_delete) {
                new AlertDialog.Builder(this)
                        .setTitle("Удаление привычки")
                        .setMessage("Удалить '" + habit.getName() + "'?")
                        .setPositiveButton("Удалить", (dialog, which) -> {
                            new Thread(() -> {
                                habitDao.deleteHabit(habit);
                                runOnUiThread(() -> habitListContainer.removeView(habitView));
                            }).start();
                        })
                        .setNegativeButton("Отмена", null)
                        .show();
                return true;
            } else if (item.getItemId() == R.id.action_edit) {
                editHabit(habit, habitView);
                return true;
            }
            return false;
        });

        popup.show();
    }

    private void editHabit(Habit habit, View habitView) {
        EditText input = new EditText(this);
        input.setText(habit.getName());

        new AlertDialog.Builder(this)
                .setTitle("Редактирование привычки")
                .setView(input)
                .setPositiveButton("Сохранить", (dialog, which) -> {
                    String newName = input.getText().toString().trim();
                    if (!newName.isEmpty()) {
                        habit.setName(newName);
                        new Thread(() -> {
                            habitDao.updateHabit(habit);
                            runOnUiThread(() -> {
                                TextView habitText = habitView.findViewById(R.id.habit_text);
                                habitText.setText(newName);
                            });
                        }).start();
                    }
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

    private void loadHabits() {
        new Thread(() -> {
            List<Habit> habits = habitDao.getAllHabits();
            runOnUiThread(() -> {
                for (Habit habit : habits) {
                    addHabitToScreen(habit);
                }
            });
        }).start();
    }
}