package com.example.quotepad.nav_frags;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quotepad.R;
import com.example.quotepad.adapter.UploadedAdapter;
import com.example.quotepad.model.QuotesModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UploadedQuotesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UploadedQuotesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView rv;
    UploadedAdapter uploadedAdapter;
    DatabaseReference reference;

    public UploadedQuotesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Discover.
     */
    // TODO: Rename and change types and number of parameters
    public static UploadedQuotesFragment newInstance(String param1, String param2) {
        UploadedQuotesFragment fragment = new UploadedQuotesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_uploaded_quotes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reference = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("quote");

        rv = getActivity().findViewById(R.id.discover_rv);

        // To display the Recycler view linearly
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        // It is a class provide by the FirebaseUI to make a
        // query in the database to fetch appropriate data
        FirebaseRecyclerOptions<QuotesModel> options
                = new FirebaseRecyclerOptions.Builder<QuotesModel>()
                .setQuery(reference, QuotesModel.class)
                .build();
        // Connecting object of required Adapter class to
        // the Adapter class itself
        uploadedAdapter = new UploadedAdapter(options);
        // Connecting Adapter class with the Recycler view*/
        rv.setAdapter(uploadedAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        uploadedAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        uploadedAdapter.stopListening();
    }
}