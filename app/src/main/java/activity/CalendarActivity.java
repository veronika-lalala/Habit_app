package activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myappp.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import db.DayHabit;
import db.DayHabitDao;
import db.HabitDataBase;

public class CalendarActivity extends AppCompatActivity {

    private TextView selectedDateTextView;
    private MaterialCalendarView materialCalendarView;
    private DayHabitDao dayHabitDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("CL", "Calendar activity create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        dayHabitDao = HabitDataBase.getInstance(this).dayHabitDao();


//        selectedDateTextView = findViewById(R.id.selectedDateTextView);
        materialCalendarView = findViewById(R.id.materialCalendarView);
        materialCalendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_NONE);

//        materialCalendarView.setOnDateChangedListener((widget, date, selected) -> {
//            String selectedDate = "Выбранная дата: " + date.getDay() + "/" + (date.getMonth() + 1) + "/" + date.getYear();
//            selectedDateTextView.setText(selectedDate);
//        });
        loadCompletedDates();
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