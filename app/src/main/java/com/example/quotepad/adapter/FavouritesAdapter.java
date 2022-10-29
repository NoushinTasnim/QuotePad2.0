package com.example.quotepad.adapter;

import android.content.Context;
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

import java.util.ArrayList;

public class FavouritesAdapter extends FirebaseRecyclerAdapter<RandomModel, FavouritesAdapter.FavViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    ArrayList<String> fav_quote;
    ArrayList<String> fav_author;
    Context context;

    public FavouritesAdapter(@NonNull FirebaseRecyclerOptions<RandomModel> options, ArrayList<String> fav_quote, ArrayList<String> fav_author, Context context) {
        super(options);
        this.fav_quote = fav_quote;
        this.fav_author = fav_author;
        this.context = context;
    }

    public FavouritesAdapter(@NonNull FirebaseRecyclerOptions<RandomModel> options, Context context) {
        super(options);
        this.context = context;
    }

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
    public void removeItem(int position) {
        fav_quote.remove(position);
        fav_author.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(String item, String item2, int position) {
        fav_quote.add(position, item);
        fav_author.add(position,item2);
        notifyItemInserted(position);
    }

    public ArrayList<String> getQuotes() {
        return fav_quote;
    }

    public ArrayList<String> getAuthors() {
        return fav_author;
    }
}
