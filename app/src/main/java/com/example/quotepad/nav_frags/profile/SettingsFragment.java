package com.example.quotepad.nav_frags.profile;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.example.quotepad.R;
import com.example.quotepad.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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
    TextInputLayout name,username, email;
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

        btn = getActivity().findViewById(R.id.up_btn);

        name = getActivity().findViewById(R.id.up_name);
        email = getActivity().findViewById(R.id.up_mail);
        username = getActivity().findViewById(R.id.up_username);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Query check = FirebaseDatabase.getInstance().getReference("users").orderByChild("id").equalTo(uid);

        check.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    na = snapshot.child(uid).child("name").getValue(String.class);
                    em = snapshot.child(uid).child("email").getValue(String.class);
                    us = snapshot.child(uid).child("username").getValue(String.class);
                    ph = snapshot.child(uid).child("phone").getValue(String.class);
                    pass = snapshot.child(uid).child("password").getValue(String.class);

                    name.getEditText().setText(na);
                    email.getEditText().setText(em);
                    username.getEditText().setText(us);
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
                            ph = snapshot.child(uid).child("phone").getValue(String.class);
                            pass = snapshot.child(uid).child("password").getValue(String.class);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progressDialog.dismiss();
                    }
                });

                String pname = name.getEditText().getText().toString().trim();
                String user = username.getEditText().getText().toString().trim();
                String mail = email.getEditText().getText().toString().trim();

                Log.i(TAG, "onClick: " + na + " " + us + " " + em);
                Log.i(TAG, "onClick: " + pname + " " + user + " " + mail);

                FirebaseAuth mAuth = FirebaseAuth.getInstance();

                String noWhiteSpace = "\\A\\w{4,15}\\z";
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                name.setErrorEnabled(false);
                username.setErrorEnabled(false);
                email.setErrorEnabled(false);

                if(TextUtils.isEmpty(pname))
                {
                    name.setError("Name field cannot be empty");
                }
                else if(TextUtils.isEmpty(user))
                {
                    username.setError("Username field cannot be empty");
                }
                else if(TextUtils.isEmpty(mail))
                {
                    email.setError("Email field cannot be empty");
                }
                else if (!user.matches(noWhiteSpace)) {
                    username.setError("Remove white spaces, length (4-15)");
                }
                else if (!mail.matches(emailPattern)) {
                    email.setError("Not valid mail address");
                }
                else if(pname.equals(na) && user.equals(us) && mail.equals(em))
                {
                    Toast.makeText(getActivity(), "Nothing to Change", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Query checkUser = FirebaseDatabase.getInstance().getReference("users").orderByChild("username").equalTo(user);

                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                if(user.equals(us))
                                {
                                    Log.i(TAG, "onDataChange: exists");
                                    changeUser(pname,user,mail);
                                }
                                else
                                {
                                    username.setError("Username already in use");
                                }
                            }
                            else
                            {
                                FirebaseDatabase.getInstance().getReference("emails").child(us).removeValue();
                                changeUser(pname,user,mail);
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

    private void getData(ProgressDialog progressDialog) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Query check = FirebaseDatabase.getInstance().getReference("users").orderByChild("id").equalTo(uid);

        check.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    na = snapshot.child(uid).child("name").getValue(String.class);
                    em = snapshot.child(uid).child("email").getValue(String.class);
                    us = snapshot.child(uid).child("username").getValue(String.class);
                    ph = snapshot.child(uid).child("phone").getValue(String.class);
                    pass = snapshot.child(uid).child("password").getValue(String.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        });
    }

    private void changeUser(String pname, String user, String mail) {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseAuth.getInstance().signOut();
        mAuth.signInWithEmailAndPassword(em, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //Toast.makeText(getActivity(), "Signed Up", Toast.LENGTH_SHORT).show();
                    String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    if(!mail.equals(em))
                    {
                        FirebaseAuth.getInstance().getCurrentUser().updateEmail(mail)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "User mail updated.");
                                            UserModel helperClass = new UserModel(pname, user, mail, pass,ph,currentuser);
                                            FirebaseDatabase.getInstance().getReference("users").child(currentuser).setValue(helperClass);

                                            us = user;
                                            na = pname;
                                            em = mail;

                                            UserModel helperClass2 = new UserModel(mail, pass, user);
                                            FirebaseDatabase.getInstance().getReference("emails").child(user).setValue(helperClass2);
                                        }
                                        else{
                                            Log.i(TAG, "onComplete: " + task.getException().toString());
                                        }
                                    }
                                });
                    }
                    else
                    {
                        Log.i(TAG, "onComplete: " + pname + " " + user + " " + mail);
                        UserModel helperClass = new UserModel(pname, user, mail, pass,ph,currentuser);
                        FirebaseDatabase.getInstance().getReference("users").child(currentuser).setValue(helperClass);

                        us = user;
                        na = pname;
                        em = mail;

                        UserModel helperClass2 = new UserModel(mail, pass, user);
                        FirebaseDatabase.getInstance().getReference("emails").child(user).setValue(helperClass2);
                    }
                }
                else {
                    try {
                        throw task.getException();
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Sorry, Could not change." + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        Toast.makeText(getActivity(), "Changed Profile", Toast.LENGTH_SHORT).show();
    }
}