package com.example.quotepad.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quotepad.R;
import com.example.quotepad.model.QuotesModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class UploadedAdapter extends FirebaseRecyclerAdapter<QuotesModel, UploadedAdapter.DiscoverViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public UploadedAdapter(@NonNull FirebaseRecyclerOptions<QuotesModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull DiscoverViewHolder holder, int position, @NonNull QuotesModel model) {

        holder.quote.setText(model.getQuotes());
        holder.time.setText("Uploaded on: " + model.getTime());
        holder.genre.setText("Genre: " + model.getType());
    }

    @NonNull
    @Override
    public DiscoverViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.quotes_fetched, parent, false);
        return new UploadedAdapter.DiscoverViewHolder(view);
    }

    public class DiscoverViewHolder extends RecyclerView.ViewHolder {

        TextView quote, author, time, genre;

        public DiscoverViewHolder(@NonNull View itemView) {
            super(itemView);

            quote = itemView.findViewById(R.id.quote_from_firebase);
            time = itemView.findViewById(R.id.date);
            genre = itemView.findViewById(R.id.genre);
        }
    }
}
