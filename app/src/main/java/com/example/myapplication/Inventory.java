package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Inventory extends AppCompatActivity {

    DataBaseHelper db;
    int Catg_ID;
    RecyclerView inventory;
    InventoryAdapter in_ad;
    TextView tv_menu_name,tv_menu_quantity;
    String original_name,original_quantity;
    private InventoryAdapter.RecyclerViewClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_inventory);

        db = new DataBaseHelper(this);

        Catg_ID = intent.getIntExtra("Category_ID",1);
        String Catg_Name = intent.getStringExtra("Category_Name");

        setTitle(Catg_Name);
        inventory = (RecyclerView) findViewById(R.id.inventory_activity);

        viewInventory();
    }
    /*public boolean onClick(AdapterView<?> adapterView, View view, int i, long l) {

        final int itemToDelete = i;
        // To delete the data from the App
        new AlertDialog.Builder(MainActivity.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Are you sure?")
                .setMessage("Do you want to delete this note?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        notes.remove(itemToDelete);
                        arrayAdapter.notifyDataSetChanged();
                        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
                        HashSet<String> set = new HashSet(MainActivity.notes);
                        sharedPreferences.edit().putStringSet("notes", set).apply();
                    }
                }).setNegativeButton("No", null).show();
        return true;
    }*/

    public void addInventory(View view) {
        ViewDialogInventory alert = new ViewDialogInventory(Catg_ID,db,this);
        alert.Dialog_Add(this, "Error de conexión al servidor");
    }


    public void editInventory(String original_name,String original_quantity)
    {
        ViewDialogInventory alert = new ViewDialogInventory(Catg_ID,db,this);
        alert.Dialog_Edit(this,original_name,original_quantity);
    }

    private void OnInventoryClick() {
        listener = new InventoryAdapter.RecyclerViewClickListener()
        {

            @Override
            public void onCLick(View view, int position){;}

            public void onChange(View v,int position)
            {
                tv_menu_name = (TextView)v.findViewById(R.id.Inventory_name);
                tv_menu_quantity = (TextView)v.findViewById(R.id.Inventory_quantity);
                original_name = tv_menu_name.getText().toString();
                original_quantity = tv_menu_quantity.getText().toString();
            }

        };

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case 10:
                editInventory(original_name,original_quantity);
                break;
            case 11:
                db.deleteTasks_Inventory(Catg_ID,original_name);
                viewInventory();
                break;
            default:
                return super.onContextItemSelected(item);
        }

        return false;
    }

    public void viewInventory()
    {
        Cursor cursor = db.viewTasks_Inventory(Catg_ID);

        if(cursor.getCount()==0){
            Toast.makeText(this,"No data to show",Toast.LENGTH_SHORT).show();
        }
        else {
            OnInventoryClick();
            in_ad = new InventoryAdapter(Inventory.this,cursor,listener);
            inventory.setAdapter(in_ad);
            inventory.setLayoutManager(new LinearLayoutManager(this));
        }

    }

  /*  public void openpopup(View view) {
        new AlertDialog.Builder(Inventory.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Are you sure?")
                .setMessage("Do you want to delete this note?")
                .setPositiveButton("Yes", null)
                        .setNegativeButton("No", null).show();
    }*/
}
