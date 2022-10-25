package com.example.quotepad.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quotepad.model.QuotesModel;
import com.example.quotepad.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class FirebaseQuotesAdapter extends FirebaseRecyclerAdapter<QuotesModel, FirebaseQuotesAdapter.viewHolder> {

    Context context;
    ArrayList<QuotesModel> list;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public FirebaseQuotesAdapter(@NonNull FirebaseRecyclerOptions<QuotesModel> options) {
        super(options);
    }


    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.quotes_fetched,parent,false);
        return new viewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull viewHolder holder, int position, @NonNull QuotesModel quotesModel) {
        holder.tv1.setText(quotesModel.getQuotes());
        Log.i(TAG, "onBindViewHolder: "+ quotesModel.getQuotes());
        holder.tv2.setText(quotesModel.getUser());
        holder.tv3.setText(quotesModel.getType());
        Log.d(TAG, "onBindViewHolder: " + quotesModel.getQuotes() + " " + quotesModel.getUser());
    }

    class viewHolder extends RecyclerView.ViewHolder{

        TextView tv1,tv2,tv3,tv4;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            tv1 = itemView.findViewById(R.id.quote_from_firebase);
            tv2 = itemView.findViewById(R.id.author);
            tv3 = itemView.findViewById(R.id.genre);
            tv4 = itemView.findViewById(R.id.date);

        }
    }
}