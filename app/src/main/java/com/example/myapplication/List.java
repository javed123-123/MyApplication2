package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class List extends AppCompatActivity {

    DataBaseHelper db;
    FloatingActionButton add_data;
    EditText add_name;

    int Catg_ID;

    ListView userlist;

    ArrayList<String> listItem;
    ArrayAdapter adapter;
    TextView tv_menu_name;
    String original_name;
    int pos;
    ListView lv;

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater =  getMenuInflater();
        inflater.inflate(R.menu.popup_menu,menu);
        lv = v.findViewById(R.id.users_list);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        pos = info.position;
        original_name =  lv.getItemAtPosition(pos).toString();

        switch (item.getItemId())
        {
            case R.id.popup_edit:
                //Toast.makeText(this,original_name,Toast.LENGTH_SHORT).show();
                ViewDialogList alert = new ViewDialogList(Catg_ID,db,List.this);
                alert.Dialog_Edit(List.this,original_name);
                break;
           /* case R.id.popup_edit:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,original_name);
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent,null);
                startActivity(shareIntent);*/

            case R.id.popup_delete:
                db.deleteTasks_List(Catg_ID,original_name);
                viewList();
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        db = new DataBaseHelper(this);

        listItem = new ArrayList<String>();
        add_data = (FloatingActionButton)findViewById(R.id.add_tasks);
        userlist = (ListView)findViewById(R.id.users_list);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItem);
        userlist.setAdapter(adapter);
        registerForContextMenu(userlist);

        Intent intent = getIntent();
        Catg_ID = intent.getIntExtra("Category_ID",1);
        String Catg_Name = intent.getStringExtra("Category_Name");

        setTitle(Catg_Name);

        viewList();

        add_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewDialogList alert = new ViewDialogList(Catg_ID,db,List.this);
                alert.Dialog_Add(List.this);
                viewList();
            }
        });
    }

    public void viewList()
    {
        Cursor cursor = db.viewTasks_List(Catg_ID);

        if(cursor.getCount()==0){
            Toast.makeText(this,"No data to show",Toast.LENGTH_SHORT).show();
        }
        else {
            listItem.clear();
            while ( cursor.moveToNext() )
                listItem.add(cursor.getString(1));
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItem);
            userlist.setAdapter(adapter);
            registerForContextMenu(userlist);
        }

    }

}