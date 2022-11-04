package com.example.quotepad.adapter;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quotepad.R;
import com.example.quotepad.model.QuotesModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DiscoverAdapter extends RecyclerView.Adapter<DiscoverAdapter.DiscoverViewHolder> {

    Context context;
    ArrayList<QuotesModel> list;

    public DiscoverAdapter(Context context, ArrayList<QuotesModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public DiscoverViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quotes_fetched, parent, false);
        return new DiscoverViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscoverViewHolder holder, int position) {
        QuotesModel model = list.get(position);

        holder.quote.setText(model.getQuote());
        Log.i(TAG, "onDataChangedfsdfdsfdsfd: " + model.getQuote() + model.getTime());
        Log.i(TAG, "onDataChange:  tttt " + model.getQuote());
        holder.genre.setText("Genre: " + model.getType());
        holder.time.setText("Uploaded on: " + model.getTime());
        holder.author.setText(" Uploaded by: " + model.getAuthor());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class DiscoverViewHolder extends RecyclerView.ViewHolder {

        TextView quote, time, genre, author;

        public DiscoverViewHolder(@NonNull View itemView) {
            super(itemView);

            quote = itemView.findViewById(R.id.quote_from_firebase);
            time = itemView.findViewById(R.id.date);
            genre = itemView.findViewById(R.id.genre);
            author = itemView.findViewById(R.id.author);
        }
    }
}
