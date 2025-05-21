package db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(
        tableName = "day_habits",
        foreignKeys = @ForeignKey(
                entity = Habit.class,
                parentColumns = "id",
                childColumns = "habit_id",
                onDelete = ForeignKey.CASCADE//если удалится в привычках то удалится и в календаре
        )
)
public class DayHabit {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "date")
    private long date;
    @ColumnInfo(name = "habit_id")
    private long habitId;

    public void setId(int id) {
        this.id = id;
    }
    public DayHabit(long date, long habitId) {
        this.date = date;
        this.habitId = habitId;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setHabitId(long habitId) {
        this.habitId = habitId;
    }

    public int getId() {
        return id;
    }

    public long getDate() {
        return date;
    }

    public long getHabitId() {
        return habitId;
    }


}
