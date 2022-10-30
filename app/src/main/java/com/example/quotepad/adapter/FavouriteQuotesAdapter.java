package com.example.quotepad.adapter;

import static android.content.ContentValues.TAG;

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
import com.example.quotepad.model.QuotesModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavouriteQuotesAdapter extends RecyclerView.Adapter<FavouriteQuotesAdapter.viewHolder>{

    Context context;
    ArrayList<QuotesModel> list;

    public FavouriteQuotesAdapter(Context context, ArrayList<QuotesModel> list) {
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
    public void onBindViewHolder(@NonNull FavouriteQuotesAdapter.viewHolder holder, int position) {
        QuotesModel model=list.get(position);

        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("fav").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        QuotesModel user=snapshot.getValue(QuotesModel.class);
                        holder.quote.setText(model.getQuote());
                        Log.i(TAG, "onDataChange: as" + model.getQuote());
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

    public void removeItem(QuotesModel item, int position) {
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

    public ArrayList<QuotesModel> getQuotes() {
        return list;
    }
}