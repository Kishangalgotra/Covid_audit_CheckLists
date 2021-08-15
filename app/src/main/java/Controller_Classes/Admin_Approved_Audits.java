package Controller_Classes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.covidauditchecklists.R;
import com.example.covidauditchecklists.recyclerview_spacer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import COMMON.FIREBASE_COMMON;
import Model.MODEL_ADMIN_PEND_AUDIT_REQ;
import Model.MODEL_ADMI_REJECTED_LIST;
import Recyclerview_Classes.RecyclerView_ADM_REJ_LIST;

public class Admin_Approved_Audits extends AppCompatActivity {

    //RECYCLERVIEW OBJECT DECLARATION
    RecyclerView recyclerView_ADM_APP_AUD_LIST ;
    public static  RecyclerView.Adapter mAdapter_ADM_APP_AUD_LIST ;
    //COMPONENTS DECLARATION
    static ProgressBar progressBar_ADM_APP_AUD_LIST ;
    //OBJECT DECLARATION
    public static List<MODEL_ADMI_REJECTED_LIST> mModelList_ADM_APP_AUD_LIST = new ArrayList<>();
    public static DatabaseReference myRef_fetch_Approved;
    public static  ValueEventListener mSendEventListnerApproved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_approved_audits);
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

        progressBar_ADM_APP_AUD_LIST = findViewById(R.id.progressBar_approved_list);
        progressBar_ADM_APP_AUD_LIST.setVisibility(View.GONE);

        recyclerview_spacer recyclerview_spacer = new recyclerview_spacer(20);
        recyclerView_ADM_APP_AUD_LIST = findViewById(R.id.admin_approved_recycler_list);
        recyclerView_ADM_APP_AUD_LIST.addItemDecoration(recyclerview_spacer);

        mAdapter_ADM_APP_AUD_LIST = new RecyclerView_ADM_REJ_LIST(getListData());
        recyclerView_ADM_APP_AUD_LIST.setAdapter(mAdapter_ADM_APP_AUD_LIST);
        recyclerView_ADM_APP_AUD_LIST.setLayoutManager(new LinearLayoutManager(this));
        mAdapter_ADM_APP_AUD_LIST.notifyDataSetChanged();

        FETCH_APPROVED_AUD_LIST(getBaseContext());
    }

    private List<MODEL_ADMI_REJECTED_LIST> getListData() {
        return mModelList_ADM_APP_AUD_LIST;
    }

    public static void FETCH_APPROVED_AUD_LIST(Context context){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef_fetch_Approved = database.getReference("APPROVED_AUDITOR_AUDIT");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String Auditor_name = ds.child("auditor_NAME").getValue(String.class);
                        String Auditor_UID = ds.child("auditor_UID").getValue(String.class);
                        String Client_UID = ds.child("client_UID").getValue(String.class);
                        String Client_name = ds.child("client_name").getValue(String.class);
                        String Client_rating = ds.child("client_rating").getValue(String.class);
                        String Client_selected_items = ds.child("client_selected_items").getValue(String.class);
                        MODEL_ADMI_REJECTED_LIST model_admi_rejected_list
                                = new MODEL_ADMI_REJECTED_LIST(Auditor_UID, Auditor_name, Client_UID, Client_name,
                                Client_rating, Client_selected_items, false);
                        mModelList_ADM_APP_AUD_LIST.add(model_admi_rejected_list);
                        // auditor_client_list.recyclerView_auditor_client.removeAllViews();
                        mAdapter_ADM_APP_AUD_LIST.notifyDataSetChanged();
                    }
                }else{
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        myRef_fetch_Approved.addValueEventListener(valueEventListener);
        mSendEventListnerApproved = valueEventListener;
    }

    public static void removeListenerApproved(){
        if(myRef_fetch_Approved != null){
            myRef_fetch_Approved.removeEventListener(mSendEventListnerApproved);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        removeListenerApproved();
        //Toast.makeText(this,"BACK PRESSED",Toast.LENGTH_SHORT).show();
        recyclerView_ADM_APP_AUD_LIST.removeAllViews();
        mModelList_ADM_APP_AUD_LIST.clear();
        mAdapter_ADM_APP_AUD_LIST.notifyDataSetChanged();
        Intent intent =  new Intent(getApplicationContext(),com.example.covidauditchecklists.admin.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeListenerApproved();
    }
}