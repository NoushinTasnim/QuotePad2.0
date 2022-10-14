package com.example.quotepad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

public class ForgetPassword extends AppCompatActivity {

    private Button btn;
    private TextInputLayout til;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        /*btn = findViewById(R.id.forgot_pass_btn);
        til = findViewById(R.id.forgot_pass_user);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("users");
                //Get all the values

                String user = til.getEditText().getText().toString().trim();

                til.setErrorEnabled(false);

                if(TextUtils.isEmpty(user))
                {
                    til.setError(" Enter username");
                }
                else
                {
                    Query checkUser = reference.orderByChild("emal").equalTo(email);

                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                username.setError("Email already in use");
                                progressBar.setVisibility(View.GONE);
                            }
                            else{
                                mAuth.createUserWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                        if (task.isSuccessful()) {

                                            Toast.makeText(getActivity(), "Registration successfull", Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);

                                            Intent intent = new Intent(getActivity(), PhoneNumberVerifyActivity.class);
                                            intent.putExtra("name", pname);
                                            intent.putExtra("user", user);
                                            intent.putExtra("mail", mail);
                                            intent.putExtra("pass", pass);

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
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });*/
    }
}