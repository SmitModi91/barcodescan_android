package com.barcodescan.android;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.StateListAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreenActivity extends AppCompatActivity {

    Animation topanimation, bottomanuimation;
    ImageView image;
    TextView logo, slogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        topanimation = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomanuimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        image = findViewById(R.id.img);
        logo = findViewById(R.id.txtlogo);
        slogo = findViewById(R.id.txtslogo);

        image.setAnimation(topanimation);
        logo.setAnimation(bottomanuimation);
        slogo.setAnimation(bottomanuimation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(SplashScreenActivity.this, InformationActivity.class));
                finish();
            }
        }, 5000);
    }
}