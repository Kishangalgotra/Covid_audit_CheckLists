package Controller_Classes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.covidauditchecklists.Admin_Checklist;
import com.example.covidauditchecklists.R;
import com.example.covidauditchecklists.admin;
import com.example.covidauditchecklists.recyclerview_spacer;
import com.example.covidauditchecklists.user_request;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Model.Model_admin_auditor;
import Recyclerview_Classes.RecyclerViewAdapter_admin_Client;
import Recyclerview_Classes.RecyclerViewAdapter_admin_auditor;

public class admin_auditor_list extends AppCompatActivity {

    //GLOBAL DECLARATION
    DatabaseReference myRef_admin_auditor = FirebaseDatabase.getInstance().getReference("AVAILABLE_AUDITORS");
    ValueEventListener fetch_listener;
    //RECYCLERVIEW OBJECT DECLARATION
    RecyclerView recyclerView_admin_auditor;
    private RecyclerView.Adapter mAdapter_recyclerView_admin_AUDITOR;
    //COMPONENTS DECLARATION
    Button assign_Auditor;
    static ProgressBar progressBar_admin_auditor_list;
    //OBJECT DECLARATION
    private List<Model_admin_auditor> mModelList_admin_auditor;
    public static String AUDITOR_ID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_auditor_list);
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
        progressBar_admin_auditor_list = findViewById(R.id.progressBar_admin_auditor_list);
        progressBar_admin_auditor_list.setVisibility(View.GONE);
        FETCH_AUDITORS_NAMES();

        assign_Auditor =  findViewById(R.id.assign_auditor_2);
        assign_Auditor.setOnClickListener(v -> {
            FILL_AUDITOR_CLIENT_LIST();
        });

        recyclerview_spacer recyclerview_spacer = new recyclerview_spacer(20);
        recyclerView_admin_auditor = (RecyclerView) findViewById(R.id.auditor_list_2);
        recyclerView_admin_auditor.addItemDecoration(recyclerview_spacer);

        mAdapter_recyclerView_admin_AUDITOR = new RecyclerViewAdapter_admin_auditor(getListData());
        recyclerView_admin_auditor.setAdapter(mAdapter_recyclerView_admin_AUDITOR);
        recyclerView_admin_auditor.setLayoutManager(new LinearLayoutManager(this));
    }

    private List<Model_admin_auditor> getListData() {
        mModelList_admin_auditor = new ArrayList<>();
        return mModelList_admin_auditor;
    }

    //FETCH AUDITORS NAME FORM FIREBASE
    public void FETCH_AUDITORS_NAMES(){
        fetch_listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    String AUDITOR_Name = ds.child("user_Nam").getValue().toString();
                    String AUDITOR_ID = ds.child("user_ID").getValue().toString();
                    Log.i("AUDITOR DATA", AUDITOR_ID+AUDITOR_Name);
                    mModelList_admin_auditor.add(new Model_admin_auditor(AUDITOR_ID,AUDITOR_Name,false));
                    mAdapter_recyclerView_admin_AUDITOR.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        };
        myRef_admin_auditor.addValueEventListener(fetch_listener);
    }

    //CODE TO FILL_AUDITOR_CLIENT DATABASE
    public  void FILL_AUDITOR_CLIENT_LIST(){
        new AlertDialog.Builder(this).setIcon(R.drawable.ic_baseline_check_circle_24)
          .setTitle("Are you sure to assign ?").setPositiveButton("Yes", (dialog, which) -> {
            Intent intent =  new Intent(getApplicationContext(), admin.class);
            FirebaseDatabase.getInstance().getReference("AUDITOR_CLIENT_LIST").child(AUDITOR_ID)
               .child(admin_client_list.passing_Model_admin_CLIENT.getUser_ID())
               .setValue(admin_client_list.passing_Model_admin_CLIENT).addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    progressBar_admin_auditor_list.setVisibility(View.GONE);
                    REMOVE_CLIENT_FROM_ADMIN_LIST();
                    startActivity(intent);
                    finish();
                }
                else if(task.isCanceled()){
                    progressBar_admin_auditor_list.setVisibility(View.GONE);
                }
                else{
                    progressBar_admin_auditor_list.setVisibility(View.GONE);
                    Log.i("Failed","user");
                }
            });
        }).setNegativeButton("No", (dialog, which) -> {
        }).show();

    }

    //CODE TO REMOVE CLIENT FROM ADMIN LISt
    public static void REMOVE_CLIENT_FROM_ADMIN_LIST(){
        String ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference("AdminClientsRequests")
         .child(admin_client_list.passing_Model_admin_CLIENT.getUser_ID()).removeValue().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                progressBar_admin_auditor_list.setVisibility(View.GONE);
            }
            else if(task.isCanceled()){
                progressBar_admin_auditor_list.setVisibility(View.GONE);
            }
            else{
                progressBar_admin_auditor_list.setVisibility(View.GONE);
                Log.i("Failed","user");
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        myRef_admin_auditor.removeEventListener(fetch_listener);
        Intent intent = new Intent(getApplicationContext(),admin_client_list.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myRef_admin_auditor.removeEventListener(fetch_listener);
    }
}