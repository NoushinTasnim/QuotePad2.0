package com.example.quotepad;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

    private static int Splash_Screen = 3000;
    ImageView image;
    TextView logo;
    ImageView splash;

    Animation topAnim;
    Animation bottomAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        image = findViewById(R.id.logo_img);
        logo = findViewById(R.id.quotePad);
        splash = findViewById(R.id.splash_bg);

        //Animations
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_anim);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_anim);

        //Set animation to elements
        image.setAnimation(topAnim);
        logo.setAnimation(bottomAnim);

        image.animate().translationY(2400).setDuration(1000).setStartDelay(2500);
        logo.animate().translationY(1400).setDuration(1000).setStartDelay(2500);
        splash.animate().translationY(3800).setDuration(1000).setStartDelay(2500);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, TabActivity.class);
                startActivity(intent);
                finish();
            }
        },Splash_Screen);
    }
}