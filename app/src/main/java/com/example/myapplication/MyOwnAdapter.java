package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import static android.widget.Toast.*;

public class MyOwnAdapter extends RecyclerView.Adapter<MyOwnAdapter.MyOwnHolder> {
    @NonNull

    int img[];
    Context ctx;
    Cursor cursor;
    private RecyclerViewClickListener listener;
    public MyOwnAdapter(Context ct, Cursor cursor, int i1[],RecyclerViewClickListener listener){
        ctx=ct;
        this.cursor = cursor;
        img =i1;
        this.listener = listener;
    }

    @Override
    public MyOwnHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater myInflator = LayoutInflater.from(ctx);
        View myOwnView = myInflator.inflate(R.layout.my_row, parent, false);
        return new MyOwnHolder(myOwnView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOwnHolder holder, int position) {
        cursor.moveToPosition(position);
        holder.name.setText(cursor.getString(1));
        holder.description.setText(cursor.getString(2));
        int type = cursor.getInt(3);
        /*holder.t1.setText(data1[position]);
        holder.t2.setText(data2[position]);*/
        //Toast.makeText(ctx,String.valueOf(type), LENGTH_SHORT).show();
        holder.myImage.setImageResource(img[type-1]);

    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public class MyOwnHolder extends RecyclerView.ViewHolder implements View.OnClickListener, OnCreateContextMenuListener {//View.OnLongClickListener, PopupMenu.OnMenuItemClickListener,
        TextView name,description;
        ImageView myImage;
        Button button;
        CardView cardView;
        MyOwnAdapter adapter;
        public MyOwnHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.add_name);
            description = (TextView) itemView.findViewById(R.id.add_description);
            myImage = (ImageView) itemView.findViewById(R.id.Category_image);
            //button  = (Button) itemView.findViewById(R.id.change);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onCLick(view,getAdapterPosition());
            //if(view.getId()==)
            //MainActivity mainActivity = new MainActivity();
            //mainActivity.goToInventory();
            //View.generateViewId();
            // Intent intent = new Intent(ctx, Inventory.class);
            //intent.putExtra("noteId", i);
            //ctx.startActivity(intent);
            //Toast.makeText(view.getContext(), "clicked: "+view.getId(), Toast.LENGTH_SHORT).show();
        }

      /*  private void showPopupMenu(View view)
        {
            PopupMenu popupMenu = new PopupMenu(view.getContext(),view);
            popupMenu.inflate(R.menu.popup_menu);
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.show();
        }

        @Override
        public boolean onLongClick(View v) {
            listener.onLongCl
            showPopupMenu(v);
            return false;
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            return false;
        }*/

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(this.getAdapterPosition(),10,0,"Edit");
            menu.add(this.getAdapterPosition(),11,1,"Delete");
            listener.onChange(v,getAdapterPosition());
        }
    }

    public interface RecyclerViewClickListener {
        void onCLick(View view, int position) ;
        void onChange(View v, int adapterPosition);
    }
}