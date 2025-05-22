package activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myappp.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import db.DayHabit;
import db.DayHabitDao;
import db.Habit;
import db.HabitDao;
import db.HabitDataBase;

public class CalendarActivity extends AppCompatActivity {
    private MaterialCalendarView materialCalendarView;
    private DayHabitDao dayHabitDao;
    private HabitDao habitDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("CL", "Calendar activity create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        dayHabitDao = HabitDataBase.getInstance(this).dayHabitDao();


        materialCalendarView = findViewById(R.id.materialCalendarView);
        materialCalendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_SINGLE);
        materialCalendarView.setOnDateChangedListener((widget, date, selected) -> {
            long selectedDate = date.getYear() * 10000L + (date.getMonth()) * 100L + date.getDay();
            showCompletedHabitsForDate(selectedDate);
        });
        loadCompletedDates();
    }

    private void showCompletedHabitsForDate(long date) {
        new Thread(() -> {
            List<Long> habitIds = dayHabitDao.getHabitsCompletedOnDate(date);

            habitDao = HabitDataBase.getInstance(this).habitDao();
            List<Habit> habits = habitDao.getAllHabits()
                    .stream()
                    .filter(habit -> habitIds.contains(habit.getId()))
                    .collect(Collectors.toList());

            runOnUiThread(() -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Выполненные привычки на " + formatDate(date));

                if (habits.isEmpty()) {
                    builder.setMessage("В этот день не было выполнено ни одной привычки");
                } else {
                    ArrayAdapter<Habit> adapter = new ArrayAdapter<>(this, R.layout.item_habit, habits) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            if (convertView == null) {
                                convertView = getLayoutInflater().inflate(R.layout.item_habit, parent, false);
                            }

                            Habit habit = getItem(position);
                            TextView textView = convertView.findViewById(R.id.text_habit);
                            ImageView iconView = convertView.findViewById(R.id.icon_check);
                            textView.setText(habit.getName());
                            iconView.setImageResource(R.drawable.ic_check_circle);
                            return convertView;
                        }
                    };
                    builder.setAdapter(adapter, null);
                }
                builder.setPositiveButton("OK", (dialog, which) -> {
                    materialCalendarView.clearSelection();
                });
                AlertDialog dialog = builder.create();
                dialog.setOnDismissListener(d -> materialCalendarView.clearSelection());
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F0F8FF")));
                dialog.show();
            });
        }).start();
    }

    private String formatDate(long date) {
        int year = (int) (date / 10000);
        int month = (int) ((date % 10000) / 100);
        int day = (int) (date % 100);
        return String.format("%02d.%02d.%04d", day, month, year);
    }

    private void loadCompletedDates() {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<Long> dates = dayHabitDao.getAllCompletedDates();
            runOnUiThread(() -> {
                Log.d("CL", "Обновление дат: " + dates);
                if (materialCalendarView != null) {
                    materialCalendarView.removeDecorators();
                    materialCalendarView.addDecorator(new CompletedDaysDecorator(CalendarActivity.this, dates));
                    materialCalendarView.invalidateDecorators();
                }
                Log.d("CL", "завершил Обновление дат: " + dates);
            });
        });
    }


}