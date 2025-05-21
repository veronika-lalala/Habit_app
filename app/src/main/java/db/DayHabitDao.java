package db;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
import java.util.stream.Collectors;

@Dao
public interface DayHabitDao {
    @Insert
    void insert(DayHabit dayHabit);
    @Insert
    default void insertWithLogging(DayHabit dayHabit) {
        Log.d("DAY_HABIT_DAO", "Добавление новой записи: " +
                "habit_id=" + dayHabit.getHabitId() +
                ", date=" + dayHabit.getDate());
        insert(dayHabit);
    }

    // Получение ВСЕХ привычек, выполненных в указанную дату
    @Query("SELECT habit_id FROM day_habits WHERE date = :date")
    List<Long> getHabitsCompletedOnDate(long date);

    @Query("SELECT DISTINCT date FROM day_habits")
    LiveData<List<Long>> getAllCompletedDatesLive();
    @Query("SELECT DISTINCT date FROM day_habits")
    default List<Long> getAllCompletedDates() {
        Log.d("DAY_HABIT_DAO", "Запрос всех уникальных дат с выполненными привычками");

        List<Long> dates = getAllCompletedDatesInternal();

        if (dates != null) {
            Log.d("DAY_HABIT_DAO", "Найдено дат: " + dates.size());
            Log.v("DAY_HABIT_DAO", "Первые 5 дат: " +
                    dates.stream().limit(5).map(Object::toString).collect(Collectors.joining(", ")));

        } else {
            Log.w("DAY_HABIT_DAO", "Запрос вернул null!");
        }

        return dates;
    }

    @Query("SELECT DISTINCT date FROM day_habits")
    List<Long> getAllCompletedDatesInternal();
    @Query("DELETE FROM day_habits WHERE habit_id = :habitId AND date = :date")
    default void deleteByHabitAndDate(long habitId, long date) {
        Log.d("DAY_HABIT_DAO", "Удаление записи: " +
                "habit_id=" + habitId +
                ", date=" + date +
                " | Перед удалением: " +
                (getByHabitAndDate(habitId, date) != null ? "запись существует" : "запись не найдена"));

        // Выполняем удаление
        deleteByHabitAndDateInternal(habitId, date);

        Log.d("DAY_HABIT_DAO", "Проверка после удаления: " +
                (getByHabitAndDate(habitId, date) != null ? "запись осталась (ОШИБКА!)" : "запись удалена успешно"));
    }

    // Внутренний метод для реального удаления
    @Query("DELETE FROM day_habits WHERE habit_id = :habitId AND date = :date")
    void deleteByHabitAndDateInternal(long habitId, long date);
    @Query("SELECT * FROM day_habits WHERE habit_id = :habitId AND date = :date LIMIT 1")
    DayHabit getByHabitAndDate(long habitId, long date);
}