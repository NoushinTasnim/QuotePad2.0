package com.example.quotepad;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class OTPVerifyActivity extends AppCompatActivity {

    private Button btn;
    //private PinView pinView;
    private TextInputLayout otp;
    private ProgressBar progressBar;
    private TextView tv;

    String pname, user, mail, pass, phoneNo, from;

    private String verifyCode;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverify);

        pname = getIntent().getStringExtra("pname");
        user = getIntent().getStringExtra("user");
        mail = getIntent().getStringExtra("mail");
        pass = getIntent().getStringExtra("pass");
        phoneNo = getIntent().getStringExtra("phone");
        from = getIntent().getStringExtra("from");

        Log.i(TAG, "onCreate: "+ mail+" "+ pass+" "+phoneNo+ " "+ pname);

        //Log.i(TAG, "onCreate: "+user);

        btn = findViewById(R.id.verify_pass_code);
        //pinView = findViewById(R.id.pin_view);
        otp = findViewById(R.id.otp_verify);
        progressBar = findViewById(R.id.prog_bar);
        tv = findViewById(R.id.number_txt);

        tv.setText(phoneNo);

        //Toast.makeText(OTPVerifyActivity.this, phoneNo, Toast.LENGTH_SHORT).show();

        firebaseAuth = FirebaseAuth.getInstance();

        sendVerificationCodeToUser(phoneNo);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = otp.getEditText().getText().toString().trim();
                if (code.isEmpty() || code.length() < 6) {
                    otp.setError("THE CODE MUST BE 6 DIGITS");
                    otp.requestFocus();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }
        });
    }

    private void sendVerificationCodeToUser(String phoneNo) {
        // [START start_phone_auth]
        Log.i(TAG, "sendVerificationCodeToUser: sent");

        progressBar.setVisibility(View.GONE);

        Toast.makeText(this, "Code is being sent", Toast.LENGTH_SHORT).show();
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber("+88" + phoneNo)       // Phone number to verify
                        .setTimeout(120L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        // [END start_phone_auth]
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                @Override
                public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(OTPVerifyActivity.this, "Code is sent", Toast.LENGTH_SHORT).show();
                    //Get the code in global variable
                    verifyCode = s;
                }

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    String code = phoneAuthCredential.getSmsCode();
                    //Log.d(TAG, "onVerificationCompleted:" + phoneAuthCredential);
                    if (code != null) {
                        progressBar.setVisibility(View.GONE);
                        otp.getEditText().setText(code);
                        verifyCode(code);
                    }
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    Toast.makeText(OTPVerifyActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            };

    private void verifyCode(String codeByUser) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verifyCode, codeByUser);
        signInTheUserByCredentials(credential);
    }

    private void signInTheUserByCredentials(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(OTPVerifyActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            rootNode = FirebaseDatabase.getInstance();
                            reference = rootNode.getReference("users");

                            Query checkUser = reference.orderByChild("username").equalTo(user);

                            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    //Log.i(TAG, "user is "+ mail);

                                    if(from.equals("phoneNumber"))
                                    {
                                        mAuth = FirebaseAuth.getInstance();

                                        mAuth.createUserWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {

                                                if (task.isSuccessful()) {

                                                    mAuth.signInWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(OTPVerifyActivity.this, "Signed Up", Toast.LENGTH_SHORT).show();
                                                                progressBar.setVisibility(View.GONE);
                                                                String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                                                UserHelperClass helperClass = new UserHelperClass(pname, user, mail, pass,phoneNo);
                                                                reference.child(currentuser).setValue(helperClass);
                                                                progressBar.setVisibility(View.GONE);

                                                                /*FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void unused) {
                                                                        Toast.makeText(OTPVerifyActivity.this, "Email sent", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });*/
                                                            } else {
                                                                progressBar.setVisibility(View.GONE);
                                                                try {
                                                                    throw task.getException();
                                                                } catch (Exception e) {
                                                                    Toast.makeText(OTPVerifyActivity.this, "Sorry, Could not sign up.", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        }
                                                    });

                                                    //Toast.makeText(getActivity(), "Registration successfull", Toast.LENGTH_SHORT).show();
                                                    progressBar.setVisibility(View.GONE);

                                                    FirebaseAuth.getInstance().signOut();

                                                    Toast.makeText(OTPVerifyActivity.this, "Signed Up", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(OTPVerifyActivity.this, UserActivity.class);
                                                    //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                                else {
                                                    try {
                                                        throw task.getException();
                                                    } catch (Exception e) {
                                                        Toast.makeText(OTPVerifyActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        progressBar.setVisibility(View.GONE);
                                                    }
                                                }
                                            }
                                        });
                                    }
                                    else if(from.equals("forgotPass"))
                                    {
                                        Log.i(TAG, "onDataChange: "+mail+" "+pass);
                                        Toast.makeText(OTPVerifyActivity.this, "Verified", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(OTPVerifyActivity.this, ForgotPassResetActivity.class);
                                        intent.putExtra("user",user);
                                        intent.putExtra("mail",mail);
                                        intent.putExtra("pass",pass);
                                        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                            //Toast.makeText(OTPVerifyActivity.this, "Your Account has been created successfully!", Toast.LENGTH_SHORT).show();

                            //progressBar.setVisibility(View.GONE);

                            //Perform Your required action here to either let the user sign In or do something required

                            //finish();
                            //Log.i(TAG, task.getException().getMessage());
                        } else {
                            Toast.makeText(OTPVerifyActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}