package com.example.myappp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HabitListActivity extends AppCompatActivity {

    private LinearLayout habitListContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_habit_list);

        habitListContainer = findViewById(R.id.habit_list_container);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }
    public void plusClick(View v){
        newHabit();
    }
    private void newHabit(){
        final EditText input = new EditText(HabitListActivity.this);
        AlertDialog.Builder builder = new AlertDialog.Builder(HabitListActivity.this);
        builder.setTitle("Добавление привычки")
                .setMessage("Введите привычку которую хотите добавить")
                .setCancelable(false)
                .setView(input)
                .setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String habitText = input.getText().toString().trim();
                        if (!habitText.isEmpty()) {
                            addHabitToScreen(habitText);
                        }
                    }
                })
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog dialog=builder.create();
        dialog.show();
    }

    private void addHabitToScreen(String habitText) {
        LayoutInflater inflater = LayoutInflater.from(this);

        View habitView = inflater.inflate(R.layout.habit_item, habitListContainer, false);
        //View inflate(идентификатор элемента, который хотим преобр в View, родит контейнер в кот добавляется созданный View, автомат добавл в контейнер или нет)

        TextView habitTextView = habitView.findViewById(R.id.habit_text);
        // ищем элемент TextView внутри созданного View (habitView), используя его идентификатор (R.id.habit_text).

        habitTextView.setText(habitText);
        //устанавливаем текст для найденного TextView

        habitListContainer.addView(habitView);
        // созданный элемент привычки (habitView) добавляется в родит контейнер (habitListContainer)
    }

}