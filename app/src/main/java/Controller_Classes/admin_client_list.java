package Controller_Classes;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covidauditchecklists.R;
import com.example.covidauditchecklists.admin;
import com.example.covidauditchecklists.recyclerview_spacer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

import Model.Model_admin_auditor;
import Recyclerview_Classes.RecyclerViewAdapter_admin_Client;

import java.util.List;

public class admin_client_list extends AppCompatActivity {
    //Passing Object for next Activity
    public static Model_admin_auditor passing_Model_admin_CLIENT = new Model_admin_auditor("","",false);
    //GLOBAL DECLARATION
    DatabaseReference myRef_admin_CLIENTS = FirebaseDatabase.getInstance().getReference("AdminClientsRequests");
    //RECYCLERVIEW OBJECT DECLARATION
    RecyclerView recyclerView_admin_client;
    private RecyclerView.Adapter mAdapter_recyclerView_admin_CLIENT;
    //COMPONENTS DECLARATION
    Button assign_Auditor;
    TextView assign_auditor_Empty;
    //OBJECT DECLARATION
    private List<Model_admin_auditor> mModelList_admin_auditor;
    public static int BTN_enable_flag = 0;
    ValueEventListener fetchClientListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_client_list);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.black));
        }
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable  = new ColorDrawable(Color.parseColor("#102F32"));
        actionBar.setBackgroundDrawable(colorDrawable);
        assign_Auditor =  findViewById(R.id.assign_auditor);
        assign_Auditor.setOnClickListener(v -> {
            if(BTN_enable_flag == 1){
                Intent intent =  new Intent(getApplicationContext(),admin_auditor_list.class);
                startActivity(intent);
                BTN_enable_flag = 0;
                finish();
            }else{
                Toast.makeText(getApplicationContext(),"No clients to assign",Toast.LENGTH_SHORT).show();
            }
        });
        fetch_clients_requests();

        assign_auditor_Empty = findViewById(R.id.assign_auditor_Empty);
        recyclerview_spacer recyclerview_spacer = new recyclerview_spacer(20);
        recyclerView_admin_client = findViewById(R.id.admin_client_list);
        recyclerView_admin_client.addItemDecoration(recyclerview_spacer);

        mAdapter_recyclerView_admin_CLIENT = new RecyclerViewAdapter_admin_Client(getListData());
        recyclerView_admin_client.setAdapter(mAdapter_recyclerView_admin_CLIENT);
        recyclerView_admin_client.setLayoutManager(new LinearLayoutManager(this));
        emptyVisibility();
    }

    private List<Model_admin_auditor> getListData() {
        mModelList_admin_auditor = new ArrayList<>();
        emptyVisibility();
        return mModelList_admin_auditor;
    }

    //FETCH CLIENT REQUESTS FROM FIREBASE
    public void fetch_clients_requests(){
          fetchClientListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String user_ID = ds.child("user_ID").getValue().toString();
                        String user_Name = ds.child("user_Nam").getValue().toString();
                        Log.i("USER REQUEST DATA", user_ID + user_Name);
                        recyclerView_admin_client.removeAllViews();
                        mModelList_admin_auditor.add(new Model_admin_auditor(user_ID, user_Name, false));
                        mAdapter_recyclerView_admin_CLIENT.notifyDataSetChanged();
                    }
                    emptyVisibility();
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        };
        myRef_admin_CLIENTS.addValueEventListener(fetchClientListener);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        myRef_admin_CLIENTS.removeEventListener(fetchClientListener);
        Intent intent = new Intent(getApplicationContext(), admin.class);
        startActivity(intent);
        BTN_enable_flag = 0;
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myRef_admin_CLIENTS.removeEventListener(fetchClientListener);
    }

    public void emptyVisibility(){
        if(mModelList_admin_auditor.size() == 0){
            assign_auditor_Empty.setVisibility(View.VISIBLE);
        }else{
            assign_auditor_Empty.setVisibility(View.INVISIBLE);
        }
    }
}