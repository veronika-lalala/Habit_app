package db;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = HabitContract.TABLE_NAME)
public class Habit {
    public long getId() {
        return id;
    }

    public boolean getIs_completed() {
        return is_completed;
    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = HabitContract.Columns.ID)
    private long id;
    @ColumnInfo(name = HabitContract.Columns.HABITNAME)
    private String name;

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIs_completed(boolean is_completed) {
        this.is_completed = is_completed;
    }

    @ColumnInfo(name = HabitContract.Columns.ISCOMPLETED)
    private boolean is_completed;

    //TODO добавить поле частоты(сколько раз в неделю выполнять) для этого нужно поменять всплывающее окно добавления привычки
    public Habit(String name){
        this.name=name;
        this.is_completed=false;
    }

    public String getName() {
        return this.name;
    }
}
