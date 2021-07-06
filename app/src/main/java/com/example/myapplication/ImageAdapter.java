package com.example.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageHolder> {
    @NonNull

    Context ctx;
    Cursor cursor;
    private ImageAdapter.RecyclerViewClickListener listener;
    public ImageAdapter(Context ct, Cursor cursor, ImageAdapter.RecyclerViewClickListener listener){
        this.cursor = cursor;
        ctx=ct;
        this.listener = listener;
        //data1=s1;
        //data2=s2;
        //img =i1;
    }

    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater myInflator = LayoutInflater.from(ctx);
        View myOwnView = myInflator.inflate(R.layout.image_recycler_view_card, parent, false);
        return new ImageHolder(myOwnView);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageHolder holder, int position) {
        cursor.moveToPosition (position);
        byte[] imgByte = cursor.getBlob(1);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
        //holder.t1.setText(data1[position]);
        //holder.t2.setText(data2[position]);
        holder.image.setImageBitmap(bitmap);

    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    /*   public class ImageHolder extends RecyclerView.ViewHolder *//*implements View.OnClickListener*//* {
        TextView t1,t2;
        ImageView myImage;
        MyOwnAdapter adapter;
        public ImageHolder(@NonNull View itemView) {
            super(itemView);
            //t1 = (TextView) itemView.findViewById(R.id.textView2);
            //t2 = (TextView) itemView.findViewById(R.id.textView3);
            myImage = (ImageView) itemView.findViewById(R.id.imageImageView);
            //itemView.setOnClickListener(this);
        };

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(ctx, Inventory.class);
            //intent.putExtra("noteId", i);
            ctx.startActivity(intent);
            //Toast.makeText(view.getContext(), "clicked: "+view.getId(), Toast.LENGTH_SHORT).show();
        }

    }*/
    public class ImageHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {
        ImageView image;
        public ImageHolder(@NonNull View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.stored_image);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        };

        @Override
        public void onClick(View v) {
            listener.onCLick(v,getAdapterPosition());
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(this.getAdapterPosition(),10,0,"Share");
            menu.add(this.getAdapterPosition(),11,1,"Delete");
            listener.onChange(v,getAdapterPosition());
        }

    }

    public interface RecyclerViewClickListener {
        void onCLick(View view, int position) ;
        void onChange(View v, int adapterPosition);
    }
}