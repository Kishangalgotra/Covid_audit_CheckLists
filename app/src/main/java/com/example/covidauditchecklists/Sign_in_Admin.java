package com.example.covidauditchecklists;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.HashMap;

public class Sign_in_Admin extends AppCompatActivity {

    Button sign_in;
    TextView wrong_cred;
    EditText passcode;
    TextView admin_wrong_credentials;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("admin");
    private FirebaseAuth mAuth;
    ProgressBar sign_in_admin_progress_bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in__admin);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.black));
        }

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#102F32"));

        actionBar.setBackgroundDrawable(colorDrawable);
        sign_in = findViewById(R.id.sign_in_button_admin);
        admin_wrong_credentials = findViewById(R.id.admin_wrong_credentials);
        sign_in_admin_progress_bar = findViewById(R.id.sign_in_admin_progress_bar);
        sign_in_admin_progress_bar.setVisibility(View.INVISIBLE);
        admin_wrong_credentials.setVisibility(View.GONE);
        passcode = findViewById(R.id.passcode_admin);
        passcode.setText("");
        sign_in.setOnClickListener(v -> {
            // Read from the database
            final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.anim);
            sign_in.startAnimation(myAnim);
            sign_in_admin_progress_bar.setVisibility(View.VISIBLE);
            admin_wrong_credentials.setVisibility(View.GONE);

            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(Long.class).toString();
                    Log.d("TAG", "Value is: " + value);
                    if(value.equals(passcode.getText().toString())){
                        sign_in_admin_progress_bar.setVisibility(View.INVISIBLE);
                        startActivity(new Intent(Sign_in_Admin.this,admin.class));
                        finish();
                    }else{
                        sign_in_admin_progress_bar.setVisibility(View.INVISIBLE);
                        admin_wrong_credentials.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("TAG", "Failed to read value.", error.toException());
                }
            });
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }
}