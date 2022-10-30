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

public class OnThisDayAdapter extends RecyclerView.Adapter<OnThisDayAdapter.MyViewHolder> {

    ArrayList<String> list;
    ArrayList<String> list2;
    ArrayList<String> list3;
    Context context;

    public OnThisDayAdapter(Context context) {
        this.context = context;
    }

    public OnThisDayAdapter(ArrayList<String> list, ArrayList<String> list2, ArrayList<String> list3, Context context) {
        this.list = list;
        this.list2 = list2;
        this.list3 = list3;
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
        holder.person.setText(list.get(position));
        holder.job.setText(list2.get(position));
        holder.year.setText(list3.get(position));

        String p = list2.get(position);

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
        return list.size();
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
