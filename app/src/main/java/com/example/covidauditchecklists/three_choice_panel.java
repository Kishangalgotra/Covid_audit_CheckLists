package com.example.covidauditchecklists;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class three_choice_panel extends AppCompatActivity {

    TextView admin,audit,user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three_choice_panel);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.black));
        }
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#102F32"));
        actionBar.setBackgroundDrawable(colorDrawable);
        admin = findViewById(R.id.admin_panel);
        audit = findViewById(R.id.auditor_Panel);
        user = findViewById(R.id.User_Panel);
        //ADMIN CLICK
        admin.setOnClickListener(v -> {
            final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.anim);
            admin.startAnimation(myAnim);
        });
        //AUDITOR CLICK
        audit.setOnClickListener(v -> {
            final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.anim);
            audit.startAnimation(myAnim);
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        });
        //USER CLICK
        user.setOnClickListener(v -> {
            final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.anim);
            user.startAnimation(myAnim);
            Intent intent = new Intent(this,MainActivity.class);
            intent.putExtra("to_user_profile",1);
            startActivity(intent);
        });
    }
}