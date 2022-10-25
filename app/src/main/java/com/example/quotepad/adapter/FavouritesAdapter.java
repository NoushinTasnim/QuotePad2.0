package com.example.quotepad.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quotepad.R;
import com.example.quotepad.model.QuotesModel;
import com.example.quotepad.model.RandomModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class FavouritesAdapter extends FirebaseRecyclerAdapter<RandomModel, FavouritesAdapter.FavViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public FavouritesAdapter(@NonNull FirebaseRecyclerOptions<RandomModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull FavViewHolder holder, int position, @NonNull RandomModel model) {
        holder.quote.setText("\"" +model.getQuote() + "\"");
        holder.author.setText(model.getAuthor());
    }

    @NonNull
    @Override
    public FavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_layout, parent, false);
        return new FavouritesAdapter.FavViewHolder(view);
    }

    public class FavViewHolder extends RecyclerView.ViewHolder {
        TextView quote, author;
        public FavViewHolder(@NonNull View itemView) {
            super(itemView);
            quote = itemView.findViewById(R.id.random_quote);
            author = itemView.findViewById(R.id.random_author);
        }
    }
}
