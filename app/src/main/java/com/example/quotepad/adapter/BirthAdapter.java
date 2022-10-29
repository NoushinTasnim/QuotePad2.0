package com.example.quotepad.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quotepad.R;

import java.util.ArrayList;

public class BirthAdapter extends RecyclerView.Adapter<BirthAdapter.MyViewHolder> {

    ArrayList<String> man;
    ArrayList<String> job;
    ArrayList<String> date;
    Context context;

    public BirthAdapter(Context context) {
        this.context = context;
    }

    public BirthAdapter(ArrayList<String> man, ArrayList<String> job, ArrayList<String> date, Context context) {
        this.man = man;
        this.job = job;
        this.date = date;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.birth_layout, parent, false);
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.person.setText(man.get(position));
        holder.job.setText(job.get(position));
        holder.year.setText(date.get(position));

        String p = job.get(position);

        Log.i("hey", p);

        // implement setOnClickListener event on item view.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // display a toast with person name on item click
                Toast.makeText(context, p, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return man.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView person, job, year;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            person = itemView.findViewById(R.id.birth_person);
            job = itemView.findViewById(R.id.birth_recog);
            year = itemView.findViewById(R.id.birth_date);
        }
    }
}
