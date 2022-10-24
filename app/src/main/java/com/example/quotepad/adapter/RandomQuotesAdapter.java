package com.example.quotepad.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quotepad.R;

import java.util.ArrayList;

public class RandomQuotesAdapter extends RecyclerView.Adapter<RandomQuotesAdapter.MyViewHolder> {

    ArrayList<String> random_quote;
    ArrayList<String> random_author;
    Context context;

    public RandomQuotesAdapter(Context context) {
        this.context = context;
    }

    public RandomQuotesAdapter(Context context, ArrayList<String> random_quote, ArrayList<String> random_author) {
        this.context = context;
        this.random_quote = random_quote;
        this.random_author = random_author;

        Log.i("here","nopes");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        Log.i("er","assdf");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // set the data in items
        holder.quo.setText(random_quote.get(position));
        holder.aut.setText(random_author.get(position));

        String p = random_quote.get(position);

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
        return random_quote.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView quo, aut;// init the item view's

        public MyViewHolder(View itemView) {
            super(itemView);

            Log.i("gt","as");
            // get the reference of item view's
            quo = (TextView) itemView.findViewById(R.id.random_quote);
            aut = (TextView) itemView.findViewById(R.id.random_author);
        }
    }


    public void removeItem(int position) {
        random_quote.remove(position);
        random_author.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(String item, String item2, int position) {
        random_quote.add(position, item);
        random_author.add(position,item2);
        notifyItemInserted(position);
    }

    public ArrayList<String> getQuotes() {
        return random_quote;
    }

    public ArrayList<String> getAuthors() {
        return random_author;
    }
}
