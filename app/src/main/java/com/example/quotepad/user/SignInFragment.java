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
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quotepad.MainActivity;
import com.example.quotepad.R;
import com.example.quotepad.forgot_pass.ForgetPasswordActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignInFragment extends Fragment {

    ImageView logo_img_qp;
    TextView logo_qp, sign_in_txt;
    LinearLayout lay1, lay2;

    Animation leftAnim;
    Animation rightAnim;

    Animation topAnim;
    Animation bottomAnim;

    private TextInputLayout user, password;
    private Button sign_in, forgot;

    ProgressBar progressBar;

    DatabaseReference reference;
    Query checkUser;
    FirebaseAuth mAuth;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    int hj = 0;

    public SignInFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignInFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignInFragment newInstance(String param1, String param2) {
        SignInFragment fragment = new SignInFragment();
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
    public void onStop() {
        super.onStop();
        if(hj==1){
            FirebaseAuth.getInstance().signOut();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_sign_in, container, false);

        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    public void onStart(){
        super.onStart();

        user = getActivity().findViewById(R.id.sign_in_user);
        password = getActivity().findViewById(R.id.sign_in_pass);

        sign_in = getActivity().findViewById(R.id.signed_in);
        forgot = getActivity().findViewById(R.id.forgot_password_btn);

        progressBar = getActivity().findViewById(R.id.sign_in_progress_bar);

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ForgetPasswordActivity.class));
            }
        });

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(getActivity(), MainActivity.class));
        }
        else
        {
            user.getEditText().setText(null);
            password.getEditText().setText(null);
        }

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get all the values
                String username = user.getEditText().getText().toString().trim();
                String pass = password.getEditText().getText().toString().trim();

                user.setErrorEnabled(false);
                password.setErrorEnabled(false);

                if(TextUtils.isEmpty(username))
                {
                    user.setError("Username field cannot be empty");
                }
                else if(TextUtils.isEmpty(pass))
                {
                    password.setError("Password field cannot be empty");
                }
                else
                {
                    progressBar.setVisibility(View.VISIBLE);

                    mAuth = FirebaseAuth.getInstance();

                    Query checkUser = FirebaseDatabase.getInstance().getReference("emails").orderByChild("username").equalTo(username);

                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                String email = snapshot.child(username).child("email").getValue(String.class);
                                //String pass_from_db = snapshot.child("password").getValue(String.class);

                                Log.i(TAG, "onDataChange: " + username + " " + email);

                                mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            hj = 0;
                                            progressBar.setVisibility(View.GONE);

                                            if(FirebaseAuth.getInstance().getCurrentUser().isEmailVerified())
                                            {
                                                Toast.makeText(getActivity(), "Signed in", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getActivity(),MainActivity.class));
                                            }
                                            else
                                            {
                                                hj = 1;
                                                Toast.makeText(getActivity(), "Please check your spam folder in your mail and verify your email address to sign in.", Toast.LENGTH_LONG).show();
                                            }
                                        
                                        } else {
                                            hj = 1;
                                            password.setError("Wrong password");
                                            Toast.makeText(getActivity(), "Incorrect Password", Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                            try {
                                                throw task.getException();
                                            } catch (Exception e) {
                                            }
                                        }
                                    }
                                });

                                if(mAuth.getCurrentUser()!=null)
                                {
                                    FirebaseAuth.getInstance().signOut();
                                    hj = 0;
                                }
                            }
                            else{
                                progressBar.setVisibility(View.GONE);
                                user.setError("Username not registered");
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