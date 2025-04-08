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
}
