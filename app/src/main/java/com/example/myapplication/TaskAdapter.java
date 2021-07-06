package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<com.example.myapplication.TaskAdapter.TaskHolder> {
    Context context;
    private ArrayList<String> task,date,time;
    private RecyclerViewClickListener listener;

    public TaskAdapter(Context ctx, ArrayList Task, ArrayList Date, ArrayList Time, RecyclerViewClickListener listener){
        this.context=ctx;
        this.task=Task;
        this.date=Date;
        this.time=Time;
        this.listener=listener;
    }
    @NonNull
    @Override
    public com.example.myapplication.TaskAdapter.TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater myInflator=LayoutInflater.from(context);
        View view =myInflator.inflate(R.layout.task_details_view,parent,false);
        return new TaskHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull com.example.myapplication.TaskAdapter.TaskHolder holder, int position) {
        holder.TASK.setText(task.get(position));
        holder.DATE.setText(date.get(position));
        holder.TIME.setText(time.get(position));
    }

    @Override
    public int getItemCount() {
        return task.size();
    }

    public class TaskHolder extends ViewHolder implements View.OnClickListener{
        TextView TASK,DATE,TIME;
        public TaskHolder(@NonNull View itemView) {
            super(itemView);
            TASK=itemView.findViewById(R.id.TASK);
            DATE=itemView.findViewById(R.id.DATE);
            TIME=itemView.findViewById(R.id.TIME);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onCLick(v,getAdapterPosition());
        }
    }

    public interface RecyclerViewClickListener{
        void onCLick(View view,int position);
    }
}
