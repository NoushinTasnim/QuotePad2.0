package com.example.quotepad.forgot_pass;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.quotepad.R;
import com.example.quotepad.user.UserActivity;
import com.google.android.gms.tasks.OnCompleteListener;
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

public class ForgotPassResetActivity extends AppCompatActivity {

    private Button btn;
    private TextInputLayout pass, con_pass;

    String res_pass, res_pass_con, user, mail, ex_pass;

    DatabaseReference reference;
    FirebaseDatabase rootNode;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass_reset);

        user = getIntent().getStringExtra("user");
        mail = getIntent().getStringExtra("mail");
        //user = "noushin";
        //mail = "noushi@sna.xi";
        ex_pass = getIntent().getStringExtra("pass");
        Log.i(TAG, "ex "+ex_pass);
        Log.i(TAG, "user "+user);
        Log.i(TAG, "mail "+mail);

        btn = findViewById(R.id.password_change_btn);
        pass = (TextInputLayout)findViewById(R.id.res_pass);
        con_pass = findViewById(R.id.con_pass);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pass.setErrorEnabled(false);
                con_pass.setErrorEnabled(false);
                String passwordVal = "^" +
                        "(?=.*[0-9])" +         //at least 1 digit
                        "(?=.*[a-z])" +         //at least 1 lower case letter
                        "(?=.*[A-Z])" +         //at least 1 upper case letter
                        "(?=.*[@#$%^&+=])" +    //at least 1 special character
                        "(?=\\S+$)" +           //no white spaces
                        ".{6,}" +               //at least 6 characters
                        "$";

                res_pass = pass.getEditText().getText().toString();

                Log.i(TAG, "onCreate: "+res_pass + "sd");
                res_pass_con = con_pass.getEditText().getText().toString().trim();
                Log.i(TAG, "onCreate: "+res_pass_con + "sd");

                if(TextUtils.isEmpty(res_pass))
                {
                    pass.setError("Password field cannot be empty");
                    //progressBar.setVisibility(View.GONE);
                }
                else if(TextUtils.isEmpty(res_pass_con))
                {
                    con_pass.setError("Rewrite your password");
                    //progressBar.setVisibility(View.GONE);
                }
                else if (!res_pass.matches(passwordVal)) {
                    pass.setError("Password should contain at least 1 digit, 1 upper case letter, 1 lower case letter, 1 special character, no white spaces and at least 6 characters");
                    //progressBar.setVisibility(View.GONE);
                }
                else if(!res_pass.equals(res_pass_con))
                {
                    con_pass.setError("Password does not match");
                }
                else
                {
                    rootNode = FirebaseDatabase.getInstance();
                    reference = rootNode.getReference("users");

                    Query checkUser = reference.orderByChild("username").equalTo(user);

                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){

                                reference.child(user).child("password").setValue(res_pass);
                                mAuth = FirebaseAuth.getInstance();
                                Log.i(TAG, "onDataChange: password on realtime updated");

                                mAuth.signInWithEmailAndPassword(mail, ex_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {

                                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                            user.updatePassword(res_pass)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Log.d(TAG, "User password updated.");
                                                            }
                                                        }
                                                    });
                                            Toast.makeText(ForgotPassResetActivity.this, "Done", Toast.LENGTH_SHORT).show();

                                        } else {
                                            try {
                                                throw task.getException();
                                            } catch (Exception e) {
                                                Toast.makeText(ForgotPassResetActivity.this, "Could not process your request", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                });
                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(ForgotPassResetActivity.this, UserActivity.class));
                                finish();

                            }
                            else{

                                Toast.makeText(ForgotPassResetActivity.this, "Could Not Process your request", Toast.LENGTH_SHORT).show();

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