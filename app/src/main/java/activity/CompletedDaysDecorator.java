package activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.example.myappp.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.Collection;
import java.util.HashSet;

public class CompletedDaysDecorator implements DayViewDecorator {
    private final HashSet<CalendarDay> dates;
    private Context context;
    public CompletedDaysDecorator(Context context, Collection<Long> dates) {
        this.context=context;
        this.dates = new HashSet<>();
        Log.d("DE", "Инициализация декоратора. Получено дат: " + dates.size());
        for (Long date : dates) {
            int year = (int)(date / 10000);
            int month = (int)((date % 10000) / 100); // Месяцы 0-11
            int day = (int)(date % 100);
            CalendarDay calendarDay = CalendarDay.from(year, month, day);
            this.dates.add(calendarDay);
            Log.v("DE", String.format("Добавлена дата: %04d-%02d-%02d (long: %d) -> %s",
                    year, month + 1, day, date, calendarDay));
        }
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        Log.d("Decorator", "Applying decoration");
        Drawable circle = ContextCompat.getDrawable(context, R.drawable.circle);
        view.setBackgroundDrawable(circle);
    }
}
