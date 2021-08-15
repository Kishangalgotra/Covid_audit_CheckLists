package com.example.covidauditchecklists;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Model.Auditing_Request_Model;
import COMMON.*;

public class user_request extends AppCompatActivity {

    String User_name = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_request);
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
        ProgressBar auditing_request_progress_bar = findViewById(R.id.auditing_request_progress_bar);
        Intent intent = getIntent();
        User_name =  intent.getStringExtra("User_name");
        auditing_request_progress_bar.setVisibility(View.VISIBLE);
        String ID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //OBJECT CREATION
        Auditing_Request_Model auditing_request_model = new Auditing_Request_Model(COMMON_DATA.sNAME_common,ID);

        //ADDING AUDITING REQUEST IN FIREBASE
        if(!(auditing_request_model.getUser_Nam().equalsIgnoreCase("") &&
          auditing_request_model.getUser_ID().equalsIgnoreCase("")))
            {
                FirebaseDatabase.getInstance().getReference("AdminClientsRequests").child(ID)
                    .setValue(auditing_request_model).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    auditing_request_progress_bar.setVisibility(View.GONE);
                    Snackbar.make(findViewById(R.id.request_root), "SuccessFull", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else if (task.isCanceled()) {
                    auditing_request_progress_bar.setVisibility(View.GONE);
                    Snackbar.make(findViewById(R.id.request_root), "Task Is Cancelled.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    auditing_request_progress_bar.setVisibility(View.GONE);
                    Log.i("Failed", "user");
                    Snackbar.make(findViewById(R.id.request_root), "Failed", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }else{
            Toast.makeText(getApplicationContext(),"Auditing request failed",Toast.LENGTH_SHORT).show();
        }
    }
}