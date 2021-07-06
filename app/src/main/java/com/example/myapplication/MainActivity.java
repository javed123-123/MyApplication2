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

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    DataBaseHelper db;
    String Email;

    RecyclerView r1;
    MyOwnAdapter ad;
    TextView tv_menu,tv_menu2;
    String catg_name_change,catg_descp_change;
    private MyOwnAdapter.RecyclerViewClickListener listener;
    int ImageResource[]= {R.drawable.list,R.drawable.inventory,R.drawable.meeting,R.drawable.images};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        r1= (RecyclerView) findViewById(R.id.Category_RecyclerView);
        Intent intent=getIntent();
        Email=intent.getStringExtra("user");
        db = new DataBaseHelper(this);
        setTitle("Dashboard");

        viewCategories();
    }

    public void LogOut(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),LogIn.class));
        finish();
    }
    public void addCategories(View view) {
        ViewDialogDashboard alert = new ViewDialogDashboard(db, MainActivity.this);
        alert.addDialog(this, "Error de conexión al servidor");
    }

    public void editCategories() {
        //Toast.makeText(this,"Main function enter", Toast.LENGTH_SHORT).show();
        ViewDialogDashboard alert = new ViewDialogDashboard(db, MainActivity.this);
        alert.editDialog(this,catg_name_change,catg_descp_change);
    }

    private void OnCategoryClick() {
        listener = new MyOwnAdapter.RecyclerViewClickListener() {
            @Override
            public void onCLick(View view, int position) {

                //Go to Activity based on Category type

                TextView textView = (TextView)view.findViewById(R.id.add_name);
                String catg = textView.getText().toString();
                int[] catg_info = db.getInfo(catg);
                if(catg_info[1]==1) {
                    Intent intent = new Intent(MainActivity.this, List.class);

                    intent.putExtra("Category_Name", catg);
                    intent.putExtra("Category_ID", catg_info[0]);
                    //intent.putExtra("Category_Type", catg_ino[1]);
                    startActivity(intent);
                }
                if(catg_info[1]==2)
                {
                    Intent intent = new Intent(MainActivity.this, Inventory.class);

                    intent.putExtra("Category_Name", catg);
                    intent.putExtra("Category_ID", catg_info[0]);
                    //intent.putExtra("Category_Type", catg_ino[1]);
                    startActivity(intent);
                }
                if(catg_info[1]==3)
                {
                    Intent intent = new Intent(MainActivity.this, Meetings.class);

                    intent.putExtra("Category_Name", catg);
                    intent.putExtra("Category_ID", catg_info[0]);
                    //intent.putExtra("Category_Type", catg_ino[1]);
                    startActivity(intent);
                }
                if(catg_info[1]==4)
                {
                    Intent intent = new Intent(MainActivity.this, Images.class);

                    intent.putExtra("Category_Name", catg);
                    intent.putExtra("Category_ID", catg_info[0]);
                    //intent.putExtra("Category_Type", catg_ino[1]);
                    startActivity(intent);
                }
            }

            public void onChange(View v,int position)
            {
                tv_menu = (TextView)v.findViewById(R.id.add_name);
                catg_name_change = tv_menu.getText().toString();
                tv_menu2 = (TextView)v.findViewById(R.id.add_description);
                catg_descp_change = tv_menu2.getText().toString();
            }
        };
    }


    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case 10:
                editCategories();
                break;
            case 11:
                db.deleteCategory(catg_name_change);
                viewCategories();
                break;
        }
        return super.onContextItemSelected(item);
    }



    public void viewCategories()
    {
        Cursor cursor = db.viewCategories();

        if(cursor.getCount()==0){
            Toast.makeText(this,"No data to show",Toast.LENGTH_SHORT).show();
        }
        else {
            OnCategoryClick();
            ad = new MyOwnAdapter(this,cursor,ImageResource,listener);
            r1.setAdapter(ad);
            r1.setLayoutManager(new LinearLayoutManager(this));
        }
    }
}