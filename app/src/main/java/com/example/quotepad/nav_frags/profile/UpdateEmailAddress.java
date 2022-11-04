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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpdateEmailAddress#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateEmailAddress extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextInputLayout email;
    Button btn;
    String name, username;

    public UpdateEmailAddress() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpdateEmailAddress.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdateEmailAddress newInstance(String param1, String param2) {
        UpdateEmailAddress fragment = new UpdateEmailAddress();
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
        return inflater.inflate(R.layout.fragment_update_email_address, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        email = getActivity().findViewById(R.id.up_mail);
        btn = getActivity().findViewById(R.id.email_change);
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ProgressDialog progressDialog = new ProgressDialog(getActivity());

                progressDialog.setMessage("Please Wait...");
                progressDialog.setTitle("Updating Email");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                String mail = email.getEditText().getText().toString().trim();

                if(TextUtils.isEmpty(mail)){
                    progressDialog.dismiss();
                    email.setError("Enter New Email");
                }
                else if(!mail.matches(emailPattern)){
                    progressDialog.dismiss();
                    email.setError("Not a valid email address");
                }
                else
                {
                    String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    FirebaseDatabase.getInstance().getReference("users").child(currentuser).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                name = snapshot.child("name").getValue(String.class);
                                username = snapshot.child("username").getValue(String.class);
                                Log.i(TAG, "onDataChange: " + snapshot.getValue());
                                if(snapshot.child("email").getValue(String.class).equals(mail))
                                {
                                    progressDialog.dismiss();
                                    Toast.makeText(getActivity(), "Nothing to change", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    UserModel helperClass = new UserModel(name, username, mail ,currentuser);
                                    UserModel helperClass2 = new UserModel(mail, username,currentuser);

                                    FirebaseAuth.getInstance().getCurrentUser().updateEmail(mail)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        progressDialog.dismiss();
                                                        Log.d(TAG, "User Email updated.");
                                                        FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                FirebaseDatabase.getInstance().getReference("users").child(currentuser).setValue(helperClass);

                                                                FirebaseDatabase.getInstance().getReference("emails").child(username).setValue(helperClass2);

                                                                Toast.makeText(getActivity(), "Verification Email sent", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }
                                                    else
                                                    {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(getActivity(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                                                        email.setError("Email already in use");
                                                    }
                                                }
                                            });
                                }
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
}