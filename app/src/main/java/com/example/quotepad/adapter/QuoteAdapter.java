package com.example.quotepad.adapter;

import static android.content.ContentValues.TAG;

import android.app.Notification;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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

    public void removeItem(RandomModel item, int position) {
        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid()).child("fav")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot: snapshot.getChildren())
                        {
                            if((item.getQuote().toString()).equals((dataSnapshot.child("quote").getValue().toString()))){
                                Log.i(TAG, "onDataChange: found ");
                                FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .child("fav").child(dataSnapshot.getKey().toString()).removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context.getApplicationContext(), error.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
        list.remove(position);
        notifyItemRemoved(position);
    }

    public ArrayList<RandomModel> getQuotes() {
        return list;
    }
}