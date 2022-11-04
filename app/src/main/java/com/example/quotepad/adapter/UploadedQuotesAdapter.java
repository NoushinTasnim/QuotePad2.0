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

public class UploadedQuotesAdapter extends RecyclerView.Adapter<UploadedQuotesAdapter.DiscoverViewHolder> {

    Context context;
    ArrayList<QuotesModel> list;
    String user = null;

    public UploadedQuotesAdapter(Context context, ArrayList<QuotesModel> list) {
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
    public void onBindViewHolder(@NonNull UploadedQuotesAdapter.DiscoverViewHolder holder, int position) {
        QuotesModel model = list.get(position);

        holder.quote.setText(model.getQuote());
        Log.i(TAG, "onDataChange:  tttt " + model.getQuote());
        holder.genre.setText("Genre: " + model.getType());
        holder.time.setText("Uploaded on: " + model.getTime());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class DiscoverViewHolder extends RecyclerView.ViewHolder {

        TextView quote,time, genre;

        public DiscoverViewHolder(@NonNull View itemView) {
            super(itemView);

            quote = itemView.findViewById(R.id.quote_from_firebase);
            time = itemView.findViewById(R.id.date);
            genre = itemView.findViewById(R.id.genre);
        }
    }

    public void removeItem(QuotesModel item, int position) {

        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        user = snapshot.child("username").getValue(String.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context.getApplicationContext(), error.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

        FirebaseDatabase.getInstance().getReference().child("quotes")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot: snapshot.getChildren())
                        {
                            Log.i(TAG, "onDataChange: xcvbn "+dataSnapshot.getValue());
                            Log.i(TAG, "onDataChange: jhb "+dataSnapshot.child("quote").getValue());
                            if((item.getQuote().toString()).equals((dataSnapshot.child("quote").getValue().toString()))){
                                Log.i(TAG, "onDataChange: found ");
                                FirebaseDatabase.getInstance().getReference("quotes").child(dataSnapshot.getKey()).removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context.getApplicationContext(), error.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid()).child("quote")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot: snapshot.getChildren())
                        {
                            Log.i(TAG, "onDataChange: xcvbn "+dataSnapshot.getValue());
                            Log.i(TAG, "onDataChange: jhb "+dataSnapshot.child("quote").getValue());
                            if((item.getQuote().toString()).equals((dataSnapshot.child("quote").getValue().toString()))){
                                Log.i(TAG, "onDataChange: found ");
                                FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .child("quote").child(dataSnapshot.getKey().toString()).removeValue();
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