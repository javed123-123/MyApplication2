package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Images extends AppCompatActivity {
    DataBaseHelper db;
    int Catg_ID;

    RecyclerView images;
    ImageAdapter in_ad;

    ImageView clicked_image;
    Bitmap clicked_image_bitmap,bitmap;
    byte[] imgByte,imgByte_clicked;

    private ImageAdapter.RecyclerViewClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_images);

        db = new DataBaseHelper(this);

        Catg_ID = intent.getIntExtra("Category_ID",1);
        String Catg_Name = intent.getStringExtra("Category_Name");

        setTitle(Catg_Name);

        images = (RecyclerView) findViewById(R.id.imagerecyclerview);
        viewImages();
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

    public void addImages(View view) {
        //Toast.makeText(Images.this,"On add click",Toast.LENGTH_SHORT).show();
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(i, 3);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
            imgByte =  outputStream.toByteArray();

            db.insertTask_Image(Catg_ID, imgByte);
            viewImages();
        }
    }
    private void OnImagesClick() {
        listener = new ImageAdapter.RecyclerViewClickListener()
        {

            @Override
            public void onCLick(View view, int position){;}

            public void onChange(View v,int position)
            {
                clicked_image = (ImageView)v.findViewById(R.id.stored_image);
                clicked_image_bitmap = ((BitmapDrawable)clicked_image.getDrawable()).getBitmap();

                ByteArrayOutputStream outputStream1 = new ByteArrayOutputStream();
                clicked_image_bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream1);
                imgByte_clicked =  outputStream1.toByteArray();
            }

        };

    }
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case 10:
                //Toast.makeText(Images.this,"Share",Toast.LENGTH_SHORT).show();
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("images/jpeg");
                String path = MediaStore.Images.Media.insertImage(getContentResolver(),clicked_image_bitmap,"Title",null);
                Uri imageuri = Uri.parse(path);
                share.putExtra(Intent.EXTRA_STREAM,imageuri);
                startActivity(Intent.createChooser(share,"Select"));
                break;
            case 11:
                boolean flag = db.deleteTasks_Images(Catg_ID, imgByte_clicked);
                viewImages();
                break;
            default:
                return super.onContextItemSelected(item);
        }

        return false;
    }
/*
    public void editInventory(String original_name, String original_quantity)
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
                *//*tv_menu_name = (TextView)v.findViewById(R.id.Inventory_name);
                tv_menu_quantity = (TextView)v.findViewById(R.id.Inventory_quantity);
                original_name = tv_menu_name.getText().toString();
                original_quantity = tv_menu_quantity.getText().toString();*//*
                clicked_image = (ImageView)v.findViewById(R.id.stored_image);
                clicked_image_bitmap = ((BitmapDrawable)clicked_image.getDrawable()).getBitmap();
            }

        };

    }*/

    public void viewImages() {
        Cursor cursor = db.viewTasks_Images(Catg_ID);
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data to show", Toast.LENGTH_SHORT).show();
        } else {
            OnImagesClick();
            in_ad = new ImageAdapter(Images.this, cursor, listener);
            images.setAdapter(in_ad);
            images.setLayoutManager(new GridLayoutManager(this,2));
        }
    }


}