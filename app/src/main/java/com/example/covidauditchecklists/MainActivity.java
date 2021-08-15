package com.example.covidauditchecklists;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import COMMON.COMMON_DATA;
import COMMON.FIREBASE_COMMON;
import Controller_Classes.auditor_client_list;

public class MainActivity extends AppCompatActivity{
    private FirebaseAuth MAUTH;
    EditText EMAIL_SIGNING;
    EditText PASSWORD_SIGNING;
    Button SIGN_IN, SIGN_UP;
    ProgressBar PROGRESSBAR;
    TextView ADMIN_SIGNING, USER_SEARCH_PAGE, AUDITOR_PANEL;
    public static TextView WRONG_CREDENTIALS;
    TextView NO_INTERNET;
    DatabaseReference  myRef2 = FirebaseDatabase.getInstance().getReference("CheckList");
    ValueEventListener checklistListener;
    int admin = 0;
    Spinner spinner;
    FirebaseDatabase DATABASE = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabase;
    String[] DATA_FOR_UER_CHECKLIST = {};
    boolean run = true;
    int POOR_NETWORK = 0;
    int TO_USER_PROFILE = 2;
    int h = 0;
    //GLOBAL DECLARATION
    DatabaseReference myRef_admin_auditor = DATABASE.getReference("AUDITOR_FOR_SIGN_IN");
    DatabaseReference myRef_admin_users = DATABASE.getReference("CLIENT_FOR_SIGN_IN");
    int authorized_in_firebase = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        Intent intent = getIntent();
        TO_USER_PROFILE =2;
        MAUTH = FirebaseAuth.getInstance();
        AUDITOR_PANEL = findViewById(R.id.auditor_Panel);
        SIGN_IN = findViewById(R.id.sign_in_button);
        NO_INTERNET = findViewById(R.id.no_inernet);
        NO_INTERNET.setVisibility(View.INVISIBLE);
        WRONG_CREDENTIALS = findViewById(R.id.wrong_credential);
        WRONG_CREDENTIALS.setVisibility(View.INVISIBLE);
        ADMIN_SIGNING = findViewById(R.id.admin_login);
        SIGN_UP = findViewById(R.id.sign_up_at_sign_in_screen);
        PROGRESSBAR = findViewById(R.id.progressBar);
        PROGRESSBAR.setVisibility(View.GONE);
        EMAIL_SIGNING = (EditText) findViewById(R.id.editTextTextEmailAddress);
        PASSWORD_SIGNING = (EditText) findViewById(R.id.editTextTextPassword);
        //temporary dropdown checking
        spinner = (Spinner) findViewById(R.id.spinner1);
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Auditor");
        categories.add("Client");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        //temporary dropdown checking
        USER_SEARCH_PAGE = findViewById(R.id.user_search_page);
        //used for auditor common password 6789
        if (TO_USER_PROFILE == 5) {
            DatabaseReference myRef = DATABASE.getReference("AuditorPass");
            final EditText edittext = new EditText(getApplicationContext());
            new AlertDialog.Builder(this, R.style.Theme_AppCompat_DayNight_Dialog_Alert).setView(edittext).setIcon(android.R.drawable.ic_menu_add)
                    .setTitle("Authenticate First").setMessage("Kindly Enter Passcode")
                    .setPositiveButton("Sign In", (dialog, which) -> {
                        PROGRESSBAR.setVisibility(View.VISIBLE);
                        myRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String value = dataSnapshot.getValue(Long.class).toString();
                                Log.d("TAG", "Value is: " + value);
                                if (value.equals(edittext.getText().toString())) {
                                    PROGRESSBAR.setVisibility(View.INVISIBLE);
                                    h = 2;
                                } else {
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    finish();
                                    PROGRESSBAR.setVisibility(View.INVISIBLE);
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError error) {
                                // Failed to read value
                                Log.w("TAG", "Failed to read value.", error.toException());
                                PROGRESSBAR.setVisibility(View.INVISIBLE);
                            }
                        });
                    })
                    .setNegativeButton("Back", (dialog, which) -> {
                        finish();
                    }).setCancelable(false).show().setCanceledOnTouchOutside(false);
        }

        //ON USER SEARCH CLICK
        USER_SEARCH_PAGE.setOnClickListener(v -> {
            final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.anim);
            USER_SEARCH_PAGE.startAnimation(myAnim);
            startActivity(new Intent(getApplicationContext(), Admin_user_checklist_details.class));
            //finish();
        });

        //SIGN IN CLICK
        SIGN_IN.setOnClickListener(v -> {
            final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.anim);
            SIGN_IN.startAnimation(myAnim);
            if (POOR_NETWORK == 0) {
                SIGN_IN_FUNCTION();
            }
        });

        //SIGN UP CLICK
        SIGN_UP.setOnClickListener(v -> {
            final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.anim);
            SIGN_UP.startAnimation(myAnim);
            startActivity(new Intent(getApplicationContext(), Sign_up.class));
            // finish();
        });

        //ADMIN SIGN IN CLICK
        ADMIN_SIGNING.setOnClickListener(v -> {
           final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.anim);
            ADMIN_SIGNING.startAnimation(myAnim);
            startActivity(new Intent(getApplicationContext(), Sign_in_Admin.class));
            finish();
        });

        USER_SEARCH_PAGE.setVisibility(View.GONE);
    }

    //SIGN IN FUNCTION WITH CHECKS
    public void SIGN_IN_FUNCTION() {
        String entity=String.valueOf(spinner.getSelectedItem());
        if(entity.equals("Client")) {
            TO_USER_PROFILE =1;
        }
        //CHECK IF DEVICE HAVE A STABLE CONNECTION
        boolean connected ;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;   //we are connected to a network
        } else
            connected = false;
        //SIGN IN ACTIVITY STARTS HERE
        if (connected) {
            NO_INTERNET.setVisibility(View.INVISIBLE);
            WRONG_CREDENTIALS.setVisibility(View.INVISIBLE);
            String email = EMAIL_SIGNING.getText().toString().trim();
            String password = PASSWORD_SIGNING.getText().toString().trim();
            if (email.isEmpty()) {
                EMAIL_SIGNING.setError("Please Fill Email");
                EMAIL_SIGNING.requestFocus();
                return;
            }
            COMMON_DATA.sEmail_common = email;
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                EMAIL_SIGNING.setError("Please fill Email Correctly");
                EMAIL_SIGNING.requestFocus();
                return;
            }
            if (password.isEmpty()) {
                PASSWORD_SIGNING.setError("Please fill password");
                PASSWORD_SIGNING.requestFocus();
                return;
            }
            EMAIL_SIGNING.setEnabled(false);
            PASSWORD_SIGNING.setEnabled(false);
            PROGRESSBAR.setVisibility(View.VISIBLE);
            Toast.makeText(MainActivity.this, "Starting Authentication.", Toast.LENGTH_SHORT).show();
            //Getting Data For User CheckList
             checklistListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        DATA_FOR_UER_CHECKLIST = dataSnapshot.getValue(String.class).split(",");
                    } else {
                        NO_INTERNET.setText("Poor Connection");
                        NO_INTERNET.setVisibility(View.VISIBLE);
                    }
                }
                @Override
                public void onCancelled(DatabaseError error) {
                    Log.w("TAG", "Failed to read value.", error.toException());
                }
            };
            myRef2.addValueEventListener(checklistListener);

           //AUTHENTICATING GIVEN CREDENTIALS
            MAUTH.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    PROGRESSBAR.setVisibility(View.INVISIBLE);
                    if (TO_USER_PROFILE == 1) {
                        WRONG_CREDENTIALS.setText("Verifying Credentials");
                        WRONG_CREDENTIALS.setVisibility(View.VISIBLE);
                        CHECK_IF_CLIENT(email);
                    } else {
                        WRONG_CREDENTIALS.setText("Verifying Credentials");
                        WRONG_CREDENTIALS.setVisibility(View.VISIBLE);
                        CHECK_IF_AUDITOR(email);
                    }
                } else {
                    PROGRESSBAR.setVisibility(View.INVISIBLE);
                    Toast.makeText(MainActivity.this, "Task Is Failed.", Toast.LENGTH_SHORT).show();
                    WRONG_CREDENTIALS.setVisibility(View.VISIBLE);
                }
                if (task.isCanceled()) {
                    PROGRESSBAR.setVisibility(View.INVISIBLE);
                    Toast.makeText(MainActivity.this, "Task Is Cancelled.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            NO_INTERNET.setText("No Internet");
            NO_INTERNET.setVisibility(View.VISIBLE);
            PROGRESSBAR.setVisibility(View.INVISIBLE);
        }
        //ENABLING DISABLED COMPONENTS
        EMAIL_SIGNING.setEnabled(true);
        PASSWORD_SIGNING.setEnabled(true);
        SIGN_IN.setEnabled(true);
    }

    private void CHECK_IF_AUDITOR(String emailss){
        Intent intent = new Intent(this, auditor_client_list.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("string-array", DATA_FOR_UER_CHECKLIST);
        CredCheck();
        myRef_admin_auditor.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    String email = ds.getValue().toString();
                    if(emailss.equalsIgnoreCase(email)){
                        startActivity(intent);
                        finish();
                        authorized_in_firebase =2;
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }

    private void CHECK_IF_CLIENT(String emails) {
        Intent intent1 = new Intent(this, Searched_user_Data.class);
        intent1.putExtra("searched_user_condition", 1);
        CredCheck();
        myRef_admin_users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String email = ds.getValue().toString();
                    if (email.equalsIgnoreCase(emails)) {
                        startActivity(intent1);
                        finish();
                        authorized_in_firebase = 2;
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }

    public void CredCheck(){
        WRONG_CREDENTIALS.setText("Verifying Credentials");
        WRONG_CREDENTIALS.setVisibility(View.VISIBLE);
        Handler handler =  new Handler();
        handler.postDelayed(() -> {
            WRONG_CREDENTIALS.setText("Wrong Credentials");
            WRONG_CREDENTIALS.setVisibility(View.VISIBLE);
        }, 3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(myRef2 != null){
           // myRef2.removeEventListener(checklistListener);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(myRef2 != null){
            //myRef2.removeEventListener(checklistListener);
        }
    }
}