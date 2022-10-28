package com.example.quotepad.verification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.quotepad.R;
import com.example.quotepad.UserProfileActivity;
import com.google.android.material.textfield.TextInputLayout;

public class PhoneNumberVerifyActivity extends AppCompatActivity {

    private TextInputLayout phone;
    private Button btn;
    private TextView tv, tv2;
    String from;

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(from.equals("settings"))
        {
            startActivity(new Intent(PhoneNumberVerifyActivity.this, UserProfileActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number_verify);

        phone = findViewById(R.id.sign_up_phone);
        btn = findViewById(R.id.get_otp);
        tv = findViewById(R.id.verify_text);
        tv2 = findViewById(R.id.verify_ph_txt);

        String pname = getIntent().getStringExtra("pname");
        String user = getIntent().getStringExtra("user");
        String mail = getIntent().getStringExtra("mail");
        String pass = getIntent().getStringExtra("pass");
        from = getIntent().getStringExtra("set");

        if(from.equals("settings"))
        {
            tv.setText("Enter New Phone Number");
            tv2.setText("");
        }
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

                    if(from.equals("settings"))
                    {
                        intent.putExtra("from","changeNumber");
                    }
                    else
                    {
                        intent.putExtra("from","phoneNumber");
                    }

                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}