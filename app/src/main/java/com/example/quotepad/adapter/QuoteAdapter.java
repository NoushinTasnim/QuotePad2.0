package com.example.quotepad.adapter;

import android.app.Notification;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quotepad.R;
import com.example.quotepad.model.RandomModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;

public class QuoteAdapter extends RecyclerView.Adapter<QuoteAdapter.viewHolder>{

    Context context;
    ArrayList<RandomModel> list;

    public QuoteAdapter(Context context, ArrayList<RandomModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_layout,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuoteAdapter.viewHolder holder, int position) {
        RandomModel model=list.get(position);

        FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("fav").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        RandomModel user=snapshot.getValue(RandomModel.class);
                        holder.quote.setText(model.getQuote());
                        holder.author.setText(model.getAuthor());
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        TextView quote, author;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            quote = itemView.findViewById(R.id.random_quote);
            author = itemView.findViewById(R.id.random_author);
        }
    }

    public void removeItem(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(RandomModel item, int position) {
        list.add(position, item);
        notifyItemInserted(position);
    }

    public ArrayList<RandomModel> getQuotes() {
        return list;
    }
}