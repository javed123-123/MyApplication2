package com.example.myapplication;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class TaskDetails extends AppCompatActivity {
    ImageButton calendar, alarm;
    Button add_or_update, delete_task;
    EditText task_name;
    TextView date, time;
    DataBaseHelper dbh;
    String TaskName, Date, Time, previous_tn;
    int i;
    static int k=1;
    Calendar cal;
    int Catg_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);
        add_or_update = findViewById(R.id.add_or_update);
        delete_task = findViewById(R.id.delete_task);
        add_or_update.setBackgroundColor(Color.BLUE);
        delete_task.setBackgroundColor(Color.RED);
        calendar = findViewById(R.id.calendar);
        alarm = findViewById(R.id.alarm);
        task_name = findViewById(R.id.task_name);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        dbh = new DataBaseHelper(this);
        cal=Calendar.getInstance();

        Intent intent = getIntent();
        String message = intent.getStringExtra("New Or Old");
        Catg_ID = intent.getIntExtra("Category_ID",3);
        setTitle(message);

        if (message.equals("New Task")) {
            i = 0;
        } else {
            i = 1;
            getTaskDetails(message);
        }

        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });

        alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog();
            }
        });


        add_or_update.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                TaskName = task_name.getText().toString();
                Time = time.getText().toString();
                Date = date.getText().toString();
                if (TaskName.equals("")) {
                    Toast.makeText(com.example.myapplication.TaskDetails.this, "Task name, cannot be empty", Toast.LENGTH_SHORT).show();
                } else if (Time.equals("")) {
                    Toast.makeText(com.example.myapplication.TaskDetails.this, "Time is not selected", Toast.LENGTH_SHORT).show();
                } else if (Date.equals("")) {
                    Toast.makeText(com.example.myapplication.TaskDetails.this, "Date is not selected", Toast.LENGTH_SHORT).show();
                } else {
                    if (i == 0) {
                        dbh.insertTask(TaskName, Date, Time, Catg_ID);
                        String TASKNAME=TaskName;
                        startAlert(cal,TASKNAME);
                    } else {
                        dbh.updateTask(previous_tn, TaskName, Date, Time, Catg_ID);
                        String TASKNAME=TaskName;
                        startAlert(cal,TASKNAME);
                    }
                    Intent intent = new Intent(com.example.myapplication.TaskDetails.this, Meetings.class);
                    Cursor cursor=dbh.viewCategories();
                    cursor.moveToPosition(Catg_ID-1);
                    String category_name=cursor.getString(1);
                    intent.putExtra("Category_ID",Catg_ID);
                    intent.putExtra("Category_Name",category_name);
                    startActivity(intent);
                }

            }
        });

        delete_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbh.delete_task(previous_tn, Catg_ID);
                Cursor cursor=dbh.viewCategories();
                cursor.moveToPosition(Catg_ID-1);
                String category_name=cursor.getString(1);
                Intent intent = new Intent(com.example.myapplication.TaskDetails.this, Meetings.class);
                intent.putExtra("Category_ID",Catg_ID);
                intent.putExtra("Category_Name",category_name);
                startActivity(intent);
            }
        });

    }

    private void showDateDialog() {
        Calendar calendar = Calendar.getInstance();
        if (i == 1) {
            String[] str = Date.split("-");
            int dd = Integer.parseInt(str[0]);
            int MM = Integer.parseInt(str[1]) - 1;
            int yyyy = Integer.parseInt(str[2]);
            calendar.set(Calendar.YEAR, yyyy);
            calendar.set(Calendar.MONTH, yyyy);
            calendar.set(Calendar.DAY_OF_MONTH, yyyy);
        }
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, month);
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String string = dayOfMonth + "-" + (month + 1) + "-" + year;
                date.setText(string);
                Date = string;
            }
        };
        new DatePickerDialog(com.example.myapplication.TaskDetails.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showTimeDialog() {
        final Calendar calendar = Calendar.getInstance();
        if (i == 1) {
            String[] str = Time.split(":");
            int HH = Integer.parseInt(str[0]);
            int mm = Integer.parseInt(str[1]);
            calendar.set(Calendar.HOUR_OF_DAY, HH);
            calendar.set(Calendar.MINUTE, mm);
        }
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                cal.set(Calendar.MINUTE, minute);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                String minute_string;
                if (minute < 10) {
                    minute_string = "0" + minute;
                } else {
                    minute_string = "" + minute;
                }
                String string = hourOfDay + ":" + minute_string;
                time.setText(string);
                Time = string;
            }
        };
        new TimePickerDialog(com.example.myapplication.TaskDetails.this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
    }

    private void getTaskDetails(String taskName) {
        previous_tn = taskName;
        Cursor cursor = dbh.viewTaskDetails(taskName,Catg_ID);
        while (cursor.moveToNext()) {
            TaskName = cursor.getString(1);
            Date = cursor.getString(2);
            Time = cursor.getString(3);
        }
        task_name.setText(TaskName);
        date.setText(Date);
        time.setText(Time);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void startAlert(Calendar call,String task) {
        k++;
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this,Broadcast.class);
        intent.putExtra("noti",task);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), k, intent, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
    }
}

