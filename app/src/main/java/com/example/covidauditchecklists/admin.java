package com.example.covidauditchecklists;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import COMMON.Flags;
import Controller_Classes.ADMIN_AUD_PEND_LIST;
import Controller_Classes.Activity_List_Of_CheckList;
import Controller_Classes.Admin_Approved_Audits;
import Controller_Classes.Admin_Rej_Aud_List;
import Controller_Classes.admin_client_list;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class admin extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button edit_checklist,user_details,fetch_list,client_requests_list_button
            ,admin_pending_auditor_list,admin_rejected_aud_list,admin_approved_auditor_list;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("CheckList");
    ImageView imageView;
    String[] data=null;
    ProgressBar progressBar2;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menu1 = getMenuInflater();
        menu1.inflate(R.menu.menu_file, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.sign_out) {
            new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Are You Sure").setMessage("Sign Out from Application")
                    .setPositiveButton("Sign Out", (dialog, which) -> {
                        mAuth.signOut();
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();
                    }).setNegativeButton("Cancel", (dialog, which) -> {

                    }).show();
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        MainActivity.WRONG_CREDENTIALS.setVisibility(View.INVISIBLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.black));
        }
        // Define ActionBar object
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#102F32"));
        actionBar.setBackgroundDrawable(colorDrawable);
        mAuth = FirebaseAuth.getInstance();
        admin_approved_auditor_list = findViewById(R.id.admin_approved_auditor_list);
        edit_checklist = findViewById(R.id.admin_modify_checklist);
        edit_checklist.setEnabled(false);
        user_details   = findViewById(R.id.admin_user_performance);
        user_details.setEnabled(false);
        client_requests_list_button   = findViewById(R.id.admin_client_requests);
        client_requests_list_button.setEnabled(false);
        progressBar2 = findViewById(R.id.progressBar2);
        progressBar2.setVisibility(View.GONE);
        fetch_data();

        //MODIFY CHECKLIST
        edit_checklist.setOnClickListener(v -> {
            final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.anim);
            edit_checklist.startAnimation(myAnim);
            Flags.auditorClientChecklist = 0;
            Intent intent = new Intent(getApplicationContext(), Controller_Classes.Activity_List_Of_CheckList.class);
            startActivity(intent);
            finish();
        });

        //USER DETAIL AFTER SEARCHING
        user_details.setOnClickListener(v -> {
            final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.anim);
            user_details.startAnimation(myAnim);
            startActivity(new Intent(getApplicationContext(),Admin_user_checklist_details.class));
            finish();
        });

        //CLIENT REQUESTS FOR AUDIT
        client_requests_list_button.setOnClickListener(v -> {
            final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.anim);
            client_requests_list_button.startAnimation(myAnim);
            startActivity(new Intent(getApplicationContext(), admin_client_list.class));
            finish();
        });

        //admin_pending_auditor_list
        admin_pending_auditor_list =  findViewById(R.id.admin_pending_auditor_list);
        admin_pending_auditor_list.setOnClickListener(v -> {
            final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.anim);
            admin_pending_auditor_list.startAnimation(myAnim);
            startActivity(new Intent(getApplicationContext(), ADMIN_AUD_PEND_LIST.class));
            finish();
        });

        //admin_pending_auditor_list
        admin_rejected_aud_list =  findViewById(R.id.admin_rejected_auditor_list);
        admin_rejected_aud_list.setOnClickListener(v -> {
            final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.anim);
            admin_rejected_aud_list.startAnimation(myAnim);
            startActivity(new Intent(getApplicationContext(), Admin_Rej_Aud_List.class));
            finish();
        });

        //admin approved audit list
        admin_approved_auditor_list.setOnClickListener(v -> {
            final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.anim);
            admin_approved_auditor_list.startAnimation(myAnim);
            startActivity(new Intent(getApplicationContext(), Admin_Approved_Audits.class));
            finish();
        });
    }

    //FETCH CHECKLIST FROM FIREBASE
    public void fetch_data(){
        progressBar2.setVisibility(View.VISIBLE);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                data = dataSnapshot.getValue(String.class).split(",");
                progressBar2.setVisibility(View.INVISIBLE);
                user_details.setEnabled(true);
                edit_checklist.setEnabled(true);
                client_requests_list_button.setEnabled(true);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                progressBar2.setVisibility(View.INVISIBLE);
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setIcon(R.drawable.ic_baseline_exit)
                .setTitle("Are you Sure you want to Log out ?").setPositiveButton("Yes", (dialog, which) -> {
            super.onBackPressed();
            Intent intent =  new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }).setNegativeButton("No", (dialog, which) -> {
        }).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}