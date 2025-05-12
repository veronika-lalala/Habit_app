package activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myappp.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.time.LocalDate;
import java.util.List;

import db.DayHabit;
import db.DayHabitDao;
import db.HabitDataBase;

public class CalendarActivity extends AppCompatActivity {

    private TextView selectedDateTextView;
    private MaterialCalendarView materialCalendarView;
    private DayHabitDao dayHabitDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("CL","Calendar activity create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        dayHabitDao = HabitDataBase.getInstance(this).dayHabitDao();
        selectedDateTextView = findViewById(R.id.selectedDateTextView);
        materialCalendarView = findViewById(R.id.materialCalendarView);

        materialCalendarView.setOnDateChangedListener((widget, date, selected) -> {
            String selectedDate = "Выбранная дата: " + date.getDay() + "/" + (date.getMonth() + 1) + "/" + date.getYear();
            selectedDateTextView.setText(selectedDate);
        });
        loadCompletedDates();
        materialCalendarView.invalidateDecorators();
        //loadCompletedDates();
    }
    private void loadCompletedDates() {
        new Thread(() -> {
            try {
                Log.d("CL","loading");
                List<Long> completedDates = dayHabitDao.getAllCompletedDates();
                Log.d("CL","получили даты");
                runOnUiThread(() -> {
                    materialCalendarView.removeDecorators();  // Удаляем старые декораторы
                    materialCalendarView.addDecorator(
                            new CompletedDaysDecorator(this, completedDates)
                    );
                    materialCalendarView.invalidateDecorators();
                });
            } catch (Exception e) {
                e.printStackTrace();

            }
        }).start();
    }

}