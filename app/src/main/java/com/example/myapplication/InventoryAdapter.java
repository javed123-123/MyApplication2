package com.example.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.MyOwnHolder> {
    @NonNull

    String data1[],data2[];
    int img[];
    Context ctx;
    Cursor cursor;
    private RecyclerViewClickListener listener;


    public InventoryAdapter(Context ct, Cursor cursor,RecyclerViewClickListener listener){
        ctx=ct;
        this.cursor = cursor;
        this.listener = listener;
    }

    @Override
    public MyOwnHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater myInflator = LayoutInflater.from(ctx);
        View myOwnView = myInflator.inflate(R.layout.inventory_row, parent, false);
        return new MyOwnHolder(myOwnView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOwnHolder holder, int position) {
        cursor.moveToPosition (position);
        holder.t1.setText(cursor.getString(1));
        holder.t2.setText(cursor.getString(2));
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public class MyOwnHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {
        TextView t1,t2;
        public MyOwnHolder(@NonNull View itemView) {
            super(itemView);
            t1 = (TextView) itemView.findViewById(R.id.Inventory_name);
            t2 = (TextView) itemView.findViewById(R.id.Inventory_quantity);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        };

        @Override
        public void onClick(View v) {
            listener.onCLick(v,getAdapterPosition());
        }

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
