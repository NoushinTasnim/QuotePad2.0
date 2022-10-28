package com.example.quotepad.forgot_pass;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.quotepad.QuoteActivity;
import com.example.quotepad.verification.OTPVerifyActivity;
import com.example.quotepad.R;
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
                    til.setError(" Enter email");
                }
                else
                {
                    reference = FirebaseDatabase.getInstance().getReference("emails");
                    Query checkUser = reference.orderByChild("username").equalTo(user);

                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                progressDialog.hide();
                                String mail = snapshot.child(user).child("email").getValue(String.class);
                                String pass = snapshot.child(user).child("password").getValue(String.class);
                                Log.i(TAG, "onDataChange: " + mail + " " + pass);

                                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                                    Log.i(TAG, "onDataChange: user");
                                    FirebaseAuth.getInstance().signOut();
                                }
                                FirebaseAuth mAuth = FirebaseAuth.getInstance();

                                mAuth.signInWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            String cur_user = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                            Log.i(TAG, "onComplete: " + cur_user);

                                            Query check = FirebaseDatabase.getInstance().getReference("users").orderByChild("id").equalTo(cur_user);
                                            check.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot1) {
                                                    if(snapshot.exists()){

                                                        String name = snapshot1.child(cur_user).child("name").getValue(String.class);
                                                        String phone = snapshot1.child(cur_user).child("phone").getValue(String.class);

                                                        Log.i(TAG, "onDataChange: " + user + " " + name + " " + phone + " " + mail + " " + pass);
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
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                    Log.i(TAG, "onCancelled: " + error   );
                                                }
                                            });
                                        }
                                        else
                                        {
                                            Log.i(TAG, "onComplete: error");
                                        }
                                    }
                                });
                            }
                            else{
                                progressDialog.hide();
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