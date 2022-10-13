package com.example.quotepad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class PhoneNumberVerifyActivity extends AppCompatActivity {

    private TextInputLayout phone;
    private Button btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number_verify);

        phone = findViewById(R.id.sign_up_phone);
        btn = findViewById(R.id.get_otp);

        String pname = getIntent().getStringExtra("pname");
        String user = getIntent().getStringExtra("user");
        String mail = getIntent().getStringExtra("mail");
        String pass = getIntent().getStringExtra("pass");

        phone.setErrorEnabled(false);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNo = phone.getEditText().getText().toString().trim();

                if (TextUtils.isEmpty(phoneNo)) {
                    phone.setError("Phone number field should not be empty");
                } else {
                    Intent intent = new Intent(PhoneNumberVerifyActivity.this, OTPVerifyActivity.class);

                    intent.putExtra("pname", pname);
                    intent.putExtra("user", user);
                    intent.putExtra("mail", mail);
                    intent.putExtra("pass", pass);
                    intent.putExtra("phone", phoneNo);

                    startActivity(intent);
                }
            }
        });
    }
}