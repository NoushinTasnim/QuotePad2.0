package com.example.quotepad.nav_frags.profile;

import static android.content.ContentValues.TAG;

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
import android.widget.Toast;

import com.example.quotepad.R;
import com.example.quotepad.forgot_pass.ForgetPasswordActivity;
import com.example.quotepad.forgot_pass.ForgotPassResetActivity;
import com.example.quotepad.user.UserActivity;
import com.example.quotepad.verification.OTPVerifyActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpdatePasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdatePasswordFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Button btn;

    FirebaseDatabase root;
    DatabaseReference reference;

    public UpdatePasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpdatePasswordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdatePasswordFragment newInstance(String param1, String param2) {
        UpdatePasswordFragment fragment = new UpdatePasswordFragment();
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
        return inflater.inflate(R.layout.fragment_update_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //btn = getActivity().findViewById(R.id.change_pass_btn);
        btn = getActivity().findViewById(R.id.send_via_mail);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().sendPasswordResetEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "Email sent.");
                                    Toast.makeText(getActivity(), "We've sent a password reset email to " + FirebaseAuth.getInstance().getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Log.i(TAG, "onComplete: " + task.getException().toString());
                                    Toast.makeText(getActivity(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        /*btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    reference = FirebaseDatabase.getInstance().getReference("users");
                    Query checkUser = reference.orderByChild("id").equalTo(uid);

                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                String phone = snapshot.child(uid).child("phone").getValue(String.class);
                                String mail = snapshot.child(uid).child("email").getValue(String.class);

                                Log.i(TAG, "onDataChange: " + phone);

                                Intent intent = new Intent(getActivity(), OTPVerifyActivity.class);
                                intent.putExtra("phone",phone);
                                intent.putExtra("mail",mail);
                                intent.putExtra("from","reset");
                                startActivity(intent);
                            }
                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
            }
        });*/
    }
}