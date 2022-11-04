package com.example.quotepad.user;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.quotepad.model.UserModel;
import com.example.quotepad.verification.OTPVerifyActivity;
import com.example.quotepad.verification.PhoneNumberVerifyActivity;
import com.example.quotepad.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Button sign_up;
    private TextInputLayout name, username, email, password, confirm;

    private ProgressBar progressBar;

    FirebaseDatabase rootNode;
    DatabaseReference reference;
    FirebaseAuth mAuth;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SignUpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();
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
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    public void onStart(){
        super.onStart();

        name = getActivity().findViewById(R.id.sign_up_name);
        username = getActivity().findViewById(R.id.sign_up_username);
        email = getActivity().findViewById(R.id.sign_up_mail);
        password = getActivity().findViewById(R.id.sign_up_pass);
        confirm = getActivity().findViewById(R.id.confirm_pass);

        sign_up = getActivity().findViewById(R.id.sign_up_btn);

        progressBar = getActivity().findViewById(R.id.sign_up_progress_bar);

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("emails");
                //Get all the values
                String pname = name.getEditText().getText().toString().trim();
                String user = username.getEditText().getText().toString().trim();
                String mail = email.getEditText().getText().toString().trim();
                String pass = password.getEditText().getText().toString().trim();
                String con_pass = confirm.getEditText().getText().toString().trim();

                mAuth = FirebaseAuth.getInstance();

                String noWhiteSpace = "\\A\\w{4,15}\\z";
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                String passwordVal = "^" +
                        "(?=.*[0-9])" +         //at least 1 digit
                        "(?=.*[a-z])" +         //at least 1 lower case letter
                        "(?=\\S+$)" +           //no white spaces
                        ".{6,}" +               //at least 6 characters
                        "$";

                name.setErrorEnabled(false);
                username.setErrorEnabled(false);
                email.setErrorEnabled(false);
                password.setErrorEnabled(false);
                confirm.setErrorEnabled(false);

                if(TextUtils.isEmpty(pname))
                {
                    name.setError("Name field cannot be empty");
                    progressBar.setVisibility(View.GONE);
                }
                else if(TextUtils.isEmpty(user))
                {
                    username.setError("Username field cannot be empty");
                    progressBar.setVisibility(View.GONE);
                }
                else if(TextUtils.isEmpty(mail))
                {
                    email.setError("Email field cannot be empty");
                    progressBar.setVisibility(View.GONE);
                }
                else if(TextUtils.isEmpty(pass))
                {
                    password.setError("Password field cannot be empty");
                    progressBar.setVisibility(View.GONE);
                }
                else if(TextUtils.isEmpty(con_pass))
                {
                    confirm.setError("Rewrite your password");
                    progressBar.setVisibility(View.GONE);
                }
                else if (!user.matches(noWhiteSpace)) {
                    username.setError("Remove white spaces, length (4-15)");
                    progressBar.setVisibility(View.GONE);
                }
                else if (!mail.matches(emailPattern)) {
                    email.setError("Not valid mail address");
                    progressBar.setVisibility(View.GONE);
                }
                else if (!pass.matches(passwordVal)) {
                    password.setError("Password should contain at least 1 digit, 1 lower case letter, no white spaces and at least 6 characters");
                    progressBar.setVisibility(View.GONE);
                }
                else if(!pass.equals(con_pass))
                {
                    confirm.setError("Password does not match");
                    progressBar.setVisibility(View.GONE);
                }
                else
                {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.i(TAG, "onComplete: no email found");
                                progressBar.setVisibility(View.GONE);

                                Query checkUser = reference.orderByChild("username").equalTo(user);

                                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists()){
                                            username.setError("Username already in use");
                                            progressBar.setVisibility(View.GONE);

                                            if(FirebaseAuth.getInstance().getCurrentUser()!=null)
                                            {
                                                FirebaseAuth.getInstance().getCurrentUser().delete()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Log.d(TAG, "User account deleted.");
                                                                }
                                                            }
                                                        });
                                            }
                                        }
                                        else{
                                            if(FirebaseAuth.getInstance().getCurrentUser()!=null)
                                            {
                                                FirebaseAuth.getInstance().getCurrentUser().delete()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Log.d(TAG, "User account deleted.");
                                                                }
                                                            }
                                                        });
                                            }
                                            Toast.makeText(getActivity(), "Wait for a few seconds...", Toast.LENGTH_SHORT).show();
                                            FirebaseAuth.getInstance().createUserWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {

                                                    if (task.isSuccessful()) {

                                                        mAuth.signInWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                                if (task.isSuccessful()) {
                                                                    Toast.makeText(getActivity(), "Signed Up", Toast.LENGTH_SHORT).show();
                                                                    progressBar.setVisibility(View.GONE);
                                                                    String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                                                    UserModel helperClass = new UserModel(pname, user, mail, currentuser);
                                                                    rootNode.getReference("users").child(currentuser).setValue(helperClass);

                                                                    UserModel helperClass2 = new UserModel(mail, user, currentuser);
                                                                    rootNode.getReference("emails").child(user).setValue(helperClass2);
                                                                    progressBar.setVisibility(View.GONE);

                                                                    FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void unused) {
                                                                            Toast.makeText(getActivity(), "Verification Email sent", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });
                                                                } else {
                                                                    progressBar.setVisibility(View.GONE);
                                                                    try {
                                                                        throw task.getException();
                                                                    } catch (Exception e) {
                                                                        Toast.makeText(getActivity(), "Sorry, Could not sign up.", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            }
                                                        });

                                                        //Toast.makeText(getActivity(), "Registration successfull", Toast.LENGTH_SHORT).show();
                                                        progressBar.setVisibility(View.GONE);

                                                        FirebaseAuth.getInstance().signOut();

                                                        Toast.makeText(getActivity(), "Signed Up", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(getActivity(), UserActivity.class);
                                                        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        startActivity(intent);
                                                    }
                                                    else {
                                                        try {
                                                            throw task.getException();
                                                        } catch (Exception e) {
                                                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                                            progressBar.setVisibility(View.GONE);
                                                        }
                                                    }
                                                }
                                            });
                                            //startActivity(intent);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                                FirebaseAuth.getInstance().getCurrentUser().delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d(TAG, "User account deleted.");
                                                }
                                            }
                                        });
                            } else {
                                email.setError("Email already in use");

                                progressBar.setVisibility(View.GONE);
                                try {
                                    throw task.getException();
                                } catch (Exception e) {
                                    Log.i(TAG, "onComplete: " + e);
                                    //Toast.makeText(OTPVerifyActivity.this, "Sorry, Could not sign up.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }
            }
        });//Register Button method end
    }
}