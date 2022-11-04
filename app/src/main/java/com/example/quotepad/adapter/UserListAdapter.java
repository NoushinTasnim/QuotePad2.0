package com.example.quotepad.adapter;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quotepad.OtherUserActivity;
import com.example.quotepad.R;
import com.example.quotepad.model.QuotesModel;
import com.example.quotepad.model.UserModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserViewHolder>{

    Context context;
    ArrayList<UserModel> list;

    public UserListAdapter(Context context, ArrayList<UserModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_search, parent, false);
        return new UserListAdapter.UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UserModel model = list.get(position);

        holder.name.setText(model.getName());
        Log.i(TAG, "onDataChangedfsdfdsfdsfd: " + model.getName() + model.getUsername());
        holder.username.setText(model.getUsername());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // display a toast with person name on item click
                Intent intent = new Intent(view.getContext(), OtherUserActivity.class);
                intent.putExtra("name",model.getName());
                intent.putExtra("username",model.getUsername());
                intent.putExtra("id", model.getId());
                view.getContext().startActivity(intent);
                //Toast.makeText(context, p, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        TextView name, username;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.search_name);
            username = itemView.findViewById(R.id.search_username);
        }
    }
}