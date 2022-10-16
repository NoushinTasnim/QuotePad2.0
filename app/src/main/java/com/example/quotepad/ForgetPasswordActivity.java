package com.example.quotepad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ForgetPasswordActivity extends AppCompatActivity {

    private Button btn;
    private TextInputLayout til;
    private ProgressDialog progressDialog;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        btn = findViewById(R.id.forgot_pass_btn);
        til = findViewById(R.id.forgot_pass_user);

        progressDialog = new ProgressDialog(this);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("users");

                progressDialog.setMessage("Please wait while we get your information");
                progressDialog.setTitle("Log In");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                //Get all the values

                String user = til.getEditText().getText().toString().trim();

                til.setErrorEnabled(false);

                if(TextUtils.isEmpty(user))
                {
                    til.setError(" Enter username");
                }
                else
                {
                    reference = FirebaseDatabase.getInstance().getReference("users");
                    Query checkUser = reference.orderByChild("username").equalTo(user);

                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                String phone = snapshot.child(user).child("phone").getValue(String.class);
                                String pass = snapshot.child(user).child("password").getValue(String.class);
                                String mail = snapshot.child(user).child("email").getValue(String.class);
                                String name = snapshot.child(user).child("name").getValue(String.class);
                                Intent intent = new Intent(ForgetPasswordActivity.this, OTPVerifyActivity.class);

                                intent.putExtra("user", user);
                                intent.putExtra("pname", name);
                                intent.putExtra("phone", phone);
                                intent.putExtra("mail", mail);
                                intent.putExtra("pass", pass);
                                intent.putExtra("from","forgotPass");

                                startActivity(intent);
                                finish();
                            }
                            else{
                                til.setError("User not found");
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