package db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Habit.class,DayHabit.class}, version = 1, exportSchema = false)
public abstract class HabitDataBase extends RoomDatabase {

    public abstract HabitDao habitDao();
    public abstract DayHabitDao dayHabitDao();

    private static HabitDataBase instance;

    public static synchronized HabitDataBase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    HabitDataBase.class,
                    "habits_database.db"
            ).build();
        }
        return instance;
    }
}
