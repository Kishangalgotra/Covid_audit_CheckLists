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
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import COMMON.FIREBASE_COMMON;
import Model.Signin_Signup_user_data;
public class Sign_up extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private TextView email;
    private TextView password;
    private TextView retypepassword;
    private TextView match_error;
    private RadioGroup radioGroup;
    private Button signup;
    private TextView fullname;
    private ProgressBar progressBar;
    String USER_UID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
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
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.sign_up_progress_bar);
        progressBar.setVisibility(View.GONE);
        email = findViewById(R.id.sign_up_email);
        password = findViewById(R.id.sign_up_password);
        retypepassword  = findViewById(R.id.sign_up_reenter_password);
        signup = findViewById(R.id.sign_up_sign_up_button);
        signup.setOnClickListener(this);
        fullname = findViewById(R.id.sign_up_name);
        radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = radioGroup.getCheckedRadioButtonId();
                Toast.makeText(getApplicationContext(), Integer.toString(id), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void sign_up(final String fullname2, final String email2 ,final String password ,String clientORAuditor){
        progressBar.setVisibility(View.VISIBLE);
        String refrence_Of_firebase;
        if(clientORAuditor.equalsIgnoreCase("Auditors")){
            refrence_Of_firebase = "Auditors";
            ADD_ENTRY_IN_AUDITOR_FOR_SIGN_IN(email2);
        }else{
            refrence_Of_firebase = "Users";
            ADD_ENTRY_IN_CLIENT_FOR_SIGN_IN(email2);
        }

        if (!(email2.equalsIgnoreCase("") && fullname2.equalsIgnoreCase(""))) {
            USER_UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            //CODE FOR INSERTING NEW USER CREDENTIALS INF FIREBASE
            mAuth.createUserWithEmailAndPassword(email2, password).addOnCompleteListener(this, task -> {
                Log.i("Inside", "ON COMPLETE");
                if (task.isSuccessful()) {
                    String uid = task.getResult().getUser().getUid();
                    if(refrence_Of_firebase.equalsIgnoreCase("Auditors"))
                        FIREBASE_COMMON.ADD_ENTRY_IN_AVAILABLE_AUDITORS(uid);
                    Signin_Signup_user_data obj = new Signin_Signup_user_data(email2, fullname2, 0);
                    FirebaseDatabase.getInstance().getReference(refrence_Of_firebase)
                            .child(uid).setValue(obj).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            Log.i("Success", "user");
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("email", email2);
                            intent.putExtra("password", password);
                            startActivity(intent);
                            finish();
                        } else if (task1.isCanceled()) {
                            Toast.makeText(Sign_up.this, "Task Is Cancelled.", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        } else {
                            Log.i("Failed", "user");
                            Toast.makeText(Sign_up.this, "Failed.", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                } else {
                    Toast.makeText(Sign_up.this, " Authentication failed.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), " Please fill username and Email ID", Toast.LENGTH_SHORT).show();
        }
    }

    //FOR AUDITOR SIGN IN THIS FUNCTION ADD VALUE IN FIREBASE
    public void ADD_ENTRY_IN_AUDITOR_FOR_SIGN_IN(String email){
        FirebaseDatabase.getInstance().getReference("AUDITOR_FOR_SIGN_IN").push()
                .setValue(email).addOnCompleteListener(task1 -> {
            if(task1.isSuccessful()){
                progressBar.setVisibility(View.GONE);
            } else if(task1.isCanceled()){
                Toast.makeText(Sign_up.this, "Task Is Cancelled.",Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            } else{
                Log.i("Failed","user");
                Toast.makeText(Sign_up.this, "Failed.", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
    //FOR CLIENT SIGN IN THIS FUNCTION ADD VALUE IN FIREBASE
    public void ADD_ENTRY_IN_CLIENT_FOR_SIGN_IN(String email){
        FirebaseDatabase.getInstance().getReference("CLIENT_FOR_SIGN_IN").push()
                .setValue(email).addOnCompleteListener(task1 -> {
            if(task1.isSuccessful()){
                progressBar.setVisibility(View.GONE);
            } else if(task1.isCanceled()){
                Toast.makeText(Sign_up.this, "Task Is Cancelled.",Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            } else{
                Log.i("Failed","user");
                Toast.makeText(Sign_up.this, "Failed.", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.anim);
        signup.startAnimation(myAnim);
        String email1 = email.getText().toString().trim() ;
        String password1 = password.getText().toString().trim();
        String retype_password1 = retypepassword.getText().toString().trim();
        String fullname1 = fullname.getText().toString().trim();
        if(fullname1.isEmpty()) {
            fullname.setError("Please fill your Name");
            fullname.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email1).matches()){
            email.setError("Please fill Email Correctly");
            email.requestFocus();
            return;
        }
        if(!password1.equals(retype_password1)){
            retypepassword.setError("Password Did not matched");
            password.setError("Password Did not matched");
            //match_error.setVisibility(View.VISIBLE);
            return;
        }
        if(password1.length()<6) {
            password.setError("Password must be greater than 6");
            password.requestFocus();
            return ;
        }
        int id = radioGroup.getCheckedRadioButtonId();

        if(id <0){
            Toast.makeText(getApplicationContext(), "Select Client or Auditor", Toast.LENGTH_SHORT).show();
            return;
        }else{
            switch(id){
                case R.id.radio_auditor:
                    Toast.makeText(getApplicationContext(), "Auditor", Toast.LENGTH_SHORT).show();
                    sign_up(fullname1,email1,password1,"Auditors");
                    break;
                case R.id.radio_client:
                    Toast.makeText(getApplicationContext(), "Client", Toast.LENGTH_SHORT).show();
                    sign_up(fullname1,email1,password1,"Client");
                    break;
              }
        }
    }
}