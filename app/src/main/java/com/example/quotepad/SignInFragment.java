package com.example.quotepad;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private Button sign_in;

    ProgressBar progressBar;

    DatabaseReference reference;
    Query checkUser;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_sign_in, container, false);

        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    public void onStart(){
        super.onStart();

        user = getActivity().findViewById(R.id.sign_in_username);
        password = getActivity().findViewById(R.id.sign_in_pass);

        sign_in = getActivity().findViewById(R.id.signed_in);

        progressBar = getActivity().findViewById(R.id.sign_in_progress_bar);

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

                    reference = FirebaseDatabase.getInstance().getReference("users");
                    checkUser = reference.orderByChild("username").equalTo(username);

                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                String passwordFromDB = snapshot.child(username).child("password").getValue(String.class);
                                if(passwordFromDB.equals(pass)){
                                    Toast.makeText(getActivity(), "Signed in", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                                else{
                                    password.setError("Incorrect Password");
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                            else{
                                user.setError("User not registered");
                                progressBar.setVisibility(View.GONE);
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