package com.example.quotepad.nav_frags.profile;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quotepad.R;
import com.example.quotepad.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextInputLayout name;
    Button btn;
    String na, em, us, ph, pass;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please Wait");
        progressDialog.setTitle("Fetching Data...");
        progressDialog.setCanceledOnTouchOutside(false);
        //progressDialog.show();

        loadData();

        btn = getActivity().findViewById(R.id.up_btn);

        name = getActivity().findViewById(R.id.up_name);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Query check = FirebaseDatabase.getInstance().getReference("users").orderByChild("id").equalTo(uid);

        check.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    na = snapshot.child(uid).child("name").getValue(String.class);

                    name.getEditText().setText(na);
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setCanceledOnTouchOutside(false);
                //progressDialog.show();
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                Query check = FirebaseDatabase.getInstance().getReference("users").orderByChild("id").equalTo(uid);

                check.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            na = snapshot.child(uid).child("name").getValue(String.class);
                            em = snapshot.child(uid).child("email").getValue(String.class);
                            us = snapshot.child(uid).child("username").getValue(String.class);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progressDialog.dismiss();
                    }
                });

                String pname = name.getEditText().getText().toString().trim();

                Log.i(TAG, "onClick: " + na + " " + us + " " + em);

                name.setErrorEnabled(false);

                if(TextUtils.isEmpty(pname))
                {
                    name.setError("Name field cannot be empty");
                }
                else if(pname.equals(na))
                {
                    Toast.makeText(getActivity(), "Nothing to Change", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Query checkUser = FirebaseDatabase.getInstance().getReference("users").orderByChild("username").equalTo(us);

                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                Log.i(TAG, "onDataChange: exists");
                                changeUser(pname,us);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

    }

    private void changeUser(String pname, String user) {

        Log.i(TAG, "onComplete: " + pname + " " + user + " " + em);

        String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        UserModel helperClass = new UserModel(pname, user, em,currentuser);
        FirebaseDatabase.getInstance().getReference("users").child(currentuser).setValue(helperClass);

        na = pname;

        UserModel helperClass2 = new UserModel(em, user,currentuser);
        FirebaseDatabase.getInstance().getReference("emails").child(user).setValue(helperClass2);

        Toast.makeText(getActivity(), "Changed Profile", Toast.LENGTH_SHORT).show();
        //startActivity(new Intent(getActivity(),UserProfileActivity.class));
        NavigationView navigationView = getActivity().findViewById(R.id.prof_navigation_view);

        View headerView = navigationView.getHeaderView(0);

        TextView nav_name = headerView.findViewById(R.id.nav_header_name);
        nav_name.setText(pname);
    }

    public void loadData() {
        File file = getActivity().getFileStreamPath("CurrentUser.txt");

        if (file.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader( getActivity().openFileInput("CurrentUser.txt")));

                String st, qu = "";

                while ((st = reader.readLine()) != null){
                    qu = qu + st;
                }
                Log.i(TAG, "loadData: " + qu);
                String[] separated = qu.split(" ; ");
                Log.i(TAG, "loadData: " + separated[1]);
                String separated2[] = qu.split(" ; ");
                // Print the string
                Log.i(TAG, "loadData: " + separated2[1]);
                //name.getEditText().setText(separated[0]);
                reader.close();

            } catch (IOException e) {
                Toast.makeText( getActivity(), e.getMessage(), Toast.LENGTH_LONG)
                        .show();
            }
        }
    }

}