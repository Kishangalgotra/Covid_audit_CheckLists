package com.example.covidauditchecklists;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

public class Screen_Saver extends AppCompatActivity {
    int admin = 0 ;
    private FirebaseAuth mAuth;
    ProgressBar progressBar;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Users_data");
    FirebaseUser currentUser;
    Window window;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_screen__saver);
        imageView = findViewById(R.id.imageView);
        imageView.setBackgroundResource(R.drawable.green);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.screen_saver_progressBar2);
        progressBar.setVisibility(View.VISIBLE);
        CountDownTimer countDownTimer = new CountDownTimer(1000, 3) {
            @Override
            public void onTick(long millisUntilFinished) {
            }
            @Override
            public void onFinish() {
                   startActivity(new Intent(getApplicationContext(), MainActivity.class));
                   finish();
            }
        }.start();
    }

    @Override
    public void onStart() {
        super.onStart();
        imageView.setBackgroundResource(R.drawable.green);
        Log.i("OnStart", "Firebase Current User");
    }
}