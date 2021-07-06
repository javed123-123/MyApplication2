package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Meetings extends AppCompatActivity {
    DataBaseHelper dbh;
    FloatingActionButton add_task;
    RecyclerView recycler_view;
    TaskAdapter task_adapter;
    ArrayList<String> task,date,time;
    private TaskAdapter.RecyclerViewClickListener listener;
    Intent intent;
    int Catg_ID;
    String Catg_Name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetings);
        add_task=findViewById(R.id.add_task);
        task=new ArrayList<>();
        date=new ArrayList<>();
        time=new ArrayList<>();
        dbh=new DataBaseHelper(this);
        recycler_view=(RecyclerView)findViewById(R.id.recycler_view);
        intent=getIntent();
        Catg_ID = intent.getIntExtra("Category_ID",1);
        Catg_Name = intent.getStringExtra("Category_Name");
        setTitle(Catg_Name);


        getTaskDetails();

        add_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Meetings.this,TaskDetails.class);
                intent.putExtra("New Or Old","New Task");
                intent.putExtra("Category_ID", Catg_ID);
                startActivity(intent);
            }
        });
    }

    private void setTaskAdapter(){
        setOnClickListener();
        task_adapter=new TaskAdapter(this,task,date,time,listener);
        recycler_view.setAdapter(task_adapter);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setOnClickListener(){
        listener=new TaskAdapter.RecyclerViewClickListener() {
            @Override
            public void onCLick(View view, int position) {
                Intent intent=new Intent(getApplicationContext(),TaskDetails.class);
                intent.putExtra("New Or Old",task.get(position));
                intent.putExtra("Category_ID", Catg_ID);
                startActivity(intent);
            }
        };
    }

    private void getTaskDetails() {
        Cursor cursor = dbh.viewTasks(Catg_ID);
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data to show", Toast.LENGTH_SHORT).show();
        } else {
            task.clear();
            date.clear();
            time.clear();
            while (cursor.moveToNext()) {
                task.add(cursor.getString(1));
                date.add(cursor.getString(2));
                time.add(cursor.getString(3));
            }
            setTaskAdapter();
        }
    }
}