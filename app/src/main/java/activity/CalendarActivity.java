package activity;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myappp.R;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

public class CalendarActivity extends AppCompatActivity {

    private TextView selectedDateTextView;
    private MaterialCalendarView materialCalendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        selectedDateTextView = findViewById(R.id.selectedDateTextView);
        materialCalendarView = findViewById(R.id.materialCalendarView);

        materialCalendarView.setOnDateChangedListener((widget, date, selected) -> {
            String selectedDate = "Выбранная дата: " + date.getDay() + "/" + (date.getMonth() + 1) + "/" + date.getYear();
            selectedDateTextView.setText(selectedDate);
        });
    }
}