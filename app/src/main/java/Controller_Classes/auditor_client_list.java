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
import android.widget.TextView;
import android.widget.Toast;

import com.example.covidauditchecklists.MainActivity;
import com.example.covidauditchecklists.R;
import com.example.covidauditchecklists.admin;
import com.example.covidauditchecklists.recyclerview_spacer;
import com.example.covidauditchecklists.user;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import COMMON.FIREBASE_COMMON;
import COMMON.Flags;
import Model.Model_admin_auditor;
import Recyclerview_Classes.RecyclerViewAdapter_Auditor_Client_List;
import Recyclerview_Classes.RecyclerViewAdapter_admin_auditor;
import Recyclerview_Classes.RecyclerView_Pending_aud_list;

public class auditor_client_list extends AppCompatActivity  {

    //GLOBAL DECLARATION
    DatabaseReference MYREF_AUDITOR_CLIENTS = FirebaseDatabase.getInstance().getReference("AUDITOR_CLIENT_LIST");
    //RECYCLERVIEW OBJECT DECLARATION
    public static  RecyclerView recyclerView_auditor_client;
    public static  RecyclerView.Adapter mAdapter_recyclerView_AUDITOR_CLIENTS;
    public static String Clicked_user_ID = "1212";
    public static String Clicked_user_Name = "User_name";
    public static int Clicked_user_Number = -1 ;
    TextView emptyAuditorClient;
    String ID;
    //COMPONENTS DECLARATION
    Button AUDIT_CLIENT;
    static ProgressBar progressBar_auditor_client;
    ValueEventListener fetchAuditorsClientsListener;
    //OBJECT DECLARATION
    private List<Model_admin_auditor> mModelList_auditor_CLIENT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auditor_client_list);
        emptyAuditorClient = findViewById(R.id.emptyAuditorClient);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.black));
        }
        //Define ActionBar object
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#102F32"));
        actionBar.setBackgroundDrawable(colorDrawable);
        Intent intentget = getIntent();
        String[] string_array = intentget.getStringArrayExtra("string-array");
        FIREBASE_COMMON.GET_AUDITOR_NAME();
        progressBar_auditor_client = findViewById(R.id.progressBar_auditor_client);
        progressBar_auditor_client.setVisibility(View.GONE);
        FETCH_AUDITORS_CLIENTS();

        AUDIT_CLIENT =  findViewById(R.id.btn_auditor_audit_client);
        AUDIT_CLIENT.setOnClickListener(v -> {
            if(Clicked_user_Number != -1 && mModelList_auditor_CLIENT.size() != 0) {
                Flags.auditorPickCheckListFlag = 1;
                Intent intent = new Intent(getApplicationContext(), Activity_List_Of_CheckList.class);
                Flags.auditorClientChecklist = 1;
                MYREF_AUDITOR_CLIENTS.child(ID).removeEventListener(fetchAuditorsClientsListener);
                startActivity(intent);
            }else{
                Toast.makeText(getApplicationContext(),"No Client Selected",Toast.LENGTH_SHORT).show();
            }
        });
        recyclerview_spacer recyclerview_spacer = new recyclerview_spacer(20);
        recyclerView_auditor_client = (RecyclerView) findViewById(R.id.auditor_client_list);
        recyclerView_auditor_client.addItemDecoration(recyclerview_spacer);

        mAdapter_recyclerView_AUDITOR_CLIENTS = new RecyclerViewAdapter_Auditor_Client_List(getListData());
        recyclerView_auditor_client.setAdapter(mAdapter_recyclerView_AUDITOR_CLIENTS);
        recyclerView_auditor_client.setLayoutManager(new LinearLayoutManager(this));
        mAdapter_recyclerView_AUDITOR_CLIENTS.notifyDataSetChanged();
    }

    private List<Model_admin_auditor> getListData() {
        mModelList_auditor_CLIENT = new ArrayList<>();
        if(mModelList_auditor_CLIENT.size() ==0){
           // mModelList_auditor_CLIENT.add(new Model_admin_auditor("AUDITOR_ID","EMPTY CLIENT LIST",false));
        }
        return mModelList_auditor_CLIENT;
    }

    //FETCH AUDITORS CLIENTS NAME FORM FIREBASE
    public void FETCH_AUDITORS_CLIENTS(){
        ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        // progressBar2.setVisibility(View.VISIBLE);
        fetchAuditorsClientsListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String AUDITOR_Name = ds.child("user_Name").getValue().toString();
                        String AUDITOR_ID = ds.child("user_ID").getValue().toString();
                        Log.i("AUDITOR DATA", AUDITOR_ID + AUDITOR_Name);
                        mModelList_auditor_CLIENT.add(new Model_admin_auditor(AUDITOR_ID, AUDITOR_Name, false));
                        mAdapter_recyclerView_AUDITOR_CLIENTS.notifyDataSetChanged();
                    }
                    emptyVisibility();
                }else{
                    emptyVisibility();
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        };
        MYREF_AUDITOR_CLIENTS.child(ID).addValueEventListener(fetchAuditorsClientsListener);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setIcon(R.drawable.ic_baseline_exit)
                .setTitle("Are you Sure you want to Log Out ?").setPositiveButton("Yes", (dialog, which) -> {
            super.onBackPressed();
            MYREF_AUDITOR_CLIENTS.child(ID).removeEventListener(fetchAuditorsClientsListener);
            Intent intent =  new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }).setNegativeButton("No", (dialog, which) -> {
        }).show();
    }

    public void emptyVisibility(){
        if(mModelList_auditor_CLIENT.size() == 0){
            emptyAuditorClient.setVisibility(View.VISIBLE);
        }else{
            emptyAuditorClient.setVisibility(View.INVISIBLE);
        }
    }
}