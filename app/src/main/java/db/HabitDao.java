package db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface HabitDao {
    @Insert
    long insertHabit(Habit habit);
    @Update
    void updateHabit(Habit habit);
    @Query("SELECT * FROM "+HabitContract.TABLE_NAME)
    List<Habit> getAllHabits();
    @Delete
    void deleteHabit(Habit habit);

    @Query("UPDATE " + HabitContract.TABLE_NAME + " SET " + HabitContract.Columns.ISCOMPLETED + " = 0")
    void resetAllHabitsCompletion();
    @Query("SELECT COUNT(*) FROM habits")
    int getTotalHabitsCount();

    @Query("SELECT COUNT(*) FROM habits WHERE is_completed = 1 AND id IN " +
            "(SELECT habit_id FROM day_habits WHERE date = :date)")
    int getCompletedHabitsCountForDate(long date);
}
