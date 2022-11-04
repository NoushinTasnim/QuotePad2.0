package com.example.quotepad;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quotepad.adapter.DiscoverAdapter;
import com.example.quotepad.adapter.UploadedQuotesAdapter;
import com.example.quotepad.model.QuotesModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OtherUserActivity extends AppCompatActivity {

    ArrayList<QuotesModel> list=new ArrayList<>();
    private String name, username, id;
    private TextView tv1, tv2;
    private RecyclerView rv;
    UploadedQuotesAdapter adapter;
    ImageView btn;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user);

        name = getIntent().getStringExtra("name");
        username = getIntent().getStringExtra("username");
        id = getIntent().getStringExtra("id");

        tv1 = findViewById(R.id.other_name);
        tv2 = findViewById(R.id.other_username);
        rv = findViewById(R.id.other_rv);
        btn = findViewById(R.id.user_back_btn);

        tv1.setText(name);
        tv2.setText(username);

        adapter = new UploadedQuotesAdapter(this,list);

        // To display the Recycler view linearly
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);


        FirebaseDatabase.getInstance().getReference("users").child(id).child("quote")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();

                        for(DataSnapshot dataSnapshot: snapshot.getChildren())
                        {
                            if(dataSnapshot.child("publicity").getValue(String.class).equals("1"))
                            {
                                QuotesModel notification = dataSnapshot.getValue(QuotesModel.class);

                                list.add(notification);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(OtherUserActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}