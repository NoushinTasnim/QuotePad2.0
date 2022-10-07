package com.example.quotepad;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private TextInputLayout username, email, password, confirm;

    private ProgressBar progressBar;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

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
                reference = rootNode.getReference("users");
                //Get all the values
                String user = username.getEditText().getText().toString().trim();
                String mail = email.getEditText().getText().toString().trim();
                String pass = password.getEditText().getText().toString().trim();
                String con_pass = confirm.getEditText().getText().toString().trim();

                String noWhiteSpace = "\\A\\w{4,15}\\z";

                String passwordVal = "^" +
                        "(?=.*[0-9])" +         //at least 1 digit
                        "(?=.*[a-z])" +         //at least 1 lower case letter
                        "(?=.*[A-Z])" +         //at least 1 upper case letter
                        "(?=.*[@#$%^&+=])" +    //at least 1 special character
                        "(?=\\S+$)" +           //no white spaces
                        ".{4,}" +               //at least 4 characters
                        "$";

                username.setErrorEnabled(false);
                email.setErrorEnabled(false);
                password.setErrorEnabled(false);
                confirm.setErrorEnabled(false);

                if(TextUtils.isEmpty(user))
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
                else if (!pass.matches(passwordVal)) {
                    password.setError("Too Weak Password");
                    progressBar.setVisibility(View.GONE);
                }
                else if(!pass.equals(con_pass))
                {
                    confirm.setError("Password does not match");
                    progressBar.setVisibility(View.GONE);
                }
                else
                {
                    Query checkUser = reference.orderByChild("username").equalTo(user);

                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                username.setError("Username already in use");
                                progressBar.setVisibility(View.GONE);
                            }
                            else{
                                UserHelperClass helperClass = new UserHelperClass(user, mail,pass);
                                reference.child(user).setValue(helperClass);
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), "Signed Up", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });//Register Button method end
    }
}