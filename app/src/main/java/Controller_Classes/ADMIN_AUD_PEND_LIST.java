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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covidauditchecklists.R;
import com.example.covidauditchecklists.recyclerview_spacer;
import com.example.covidauditchecklists.user;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import COMMON.FIREBASE_COMMON;
import Model.MODEL_ADMIN_PEND_AUDIT_REQ;
import Recyclerview_Classes.RecyclerView_Pending_aud_list;

public class ADMIN_AUD_PEND_LIST extends AppCompatActivity  {

    public static MODEL_ADMIN_PEND_AUDIT_REQ Approved_Rejected_data ;
    public static int Clicked_DATA_position;
    //RECYCLERVIEW OBJECT DECLARATION
    public static RecyclerView recyclerView_PEND_AUD_LIST ;
    public static  RecyclerView.Adapter mAdapter_recycler_PEND_AUD_LIST ;
    //COMPONENTS DECLARATION
    public static  Button BTN_APPROVE,BTN_REJECT;
    static ProgressBar progressBar_PEND_AUD_LIST;
    public TextView pend_CheckList_Empty;
    //OBJECT DECLARATION
    public static List<MODEL_ADMIN_PEND_AUDIT_REQ> model_admin_pend_audit  = new ArrayList<>();
    //SPINNER ARRAY
    public static ArrayList<String> spinner_array;
    public static DatabaseReference myRef_fetch_Pend;
    public static  ValueEventListener mSendEventListnerPend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_pend_list);
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

        progressBar_PEND_AUD_LIST = findViewById(R.id.progressBar_admin_pend);
        progressBar_PEND_AUD_LIST.setVisibility(View.GONE);

        pend_CheckList_Empty = findViewById(R.id.pend_CheckList_Empty);

        recyclerview_spacer recyclerview_spacer = new recyclerview_spacer(20);
        recyclerView_PEND_AUD_LIST = (RecyclerView) findViewById(R.id.admin_pend_list);
        recyclerView_PEND_AUD_LIST.addItemDecoration(recyclerview_spacer);

        mAdapter_recycler_PEND_AUD_LIST = new RecyclerView_Pending_aud_list(getListData());
        recyclerView_PEND_AUD_LIST.setAdapter(mAdapter_recycler_PEND_AUD_LIST);
        recyclerView_PEND_AUD_LIST.setLayoutManager(new LinearLayoutManager(this));
        mAdapter_recycler_PEND_AUD_LIST.notifyDataSetChanged();
        FETCH_PEND_AUD_LIST(getBaseContext());
        emptyVisibility();

        //APPROVE BUTTON CLICK EVENT
        BTN_APPROVE = findViewById(R.id.approve_admin_pend);
        BTN_APPROVE.setEnabled(false);
        BTN_APPROVE.setOnClickListener(v -> {
            final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.anim);
            BTN_APPROVE.startAnimation(myAnim);
            FIREBASE_COMMON.APPROVE_AUDITOR_AUDITED_DATA(Approved_Rejected_data,getBaseContext());
            BTN_APPROVE.setEnabled(false);
            BTN_REJECT.setEnabled(false);
            emptyVisibility();
        });

        //REJECT BUTTON CLICK EVENT
        BTN_REJECT = findViewById(R.id.reject_admin_pend);
        BTN_REJECT.setEnabled(false);
        BTN_REJECT.setOnClickListener(v ->{
            final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.anim);
            BTN_REJECT.startAnimation(myAnim);
            FIREBASE_COMMON.REJECT_AUDITOR_AUDITED_DATA(Approved_Rejected_data);
            BTN_APPROVE.setEnabled(false);
            BTN_REJECT.setEnabled(false);
            emptyVisibility();
        });
    }

    //GET LIST
    private List<MODEL_ADMIN_PEND_AUDIT_REQ> getListData() {
        emptyVisibility();
        return model_admin_pend_audit;
    }

    //FETCH PENDING AUDITOR LIST NAME FORM FIREBASE
    public  void FETCH_PEND_AUD_LIST(Context context){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef_fetch_Pend = database.getReference("PEND_APPROVAL");
        ValueEventListener valueventlistenr =   new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String Auditor_name = ds.child("auditor_NAME").getValue(String.class);
                        String Auditor_UID = ds.child("auditor_UID").getValue(String.class);
                        String Client_UID = ds.child("client_UID").getValue(String.class);
                        String Client_name = ds.child("client_name").getValue(String.class);
                        String Client_rating = ds.child("client_rating").getValue(String.class);
                        String Client_selected_items = ds.child("client_selected_items").getValue(String.class);
                        String randomID = ds.child("randomUID").getValue(String.class);
                        MODEL_ADMIN_PEND_AUDIT_REQ model_admin_pend_audit_req
                                = new MODEL_ADMIN_PEND_AUDIT_REQ(Auditor_UID, Auditor_name, Client_UID, Client_name,
                                Client_rating, Client_selected_items, false);
                        model_admin_pend_audit_req.setRandomUID(randomID);
                        ADMIN_AUD_PEND_LIST.model_admin_pend_audit.add(model_admin_pend_audit_req);
                        recyclerView_PEND_AUD_LIST.removeAllViews();
                        ADMIN_AUD_PEND_LIST.mAdapter_recycler_PEND_AUD_LIST.notifyDataSetChanged();
                    }
                    emptyVisibility();
                }else{
                    emptyVisibility();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        myRef_fetch_Pend.addValueEventListener(valueventlistenr);
        mSendEventListnerPend = valueventlistenr;
    }

    public static void removeListenerPend(){
        if(myRef_fetch_Pend != null){
            myRef_fetch_Pend.removeEventListener(mSendEventListnerPend);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        removeListenerPend();
        RecyclerView_Pending_aud_list.model_admin_pend_audit_reqs.clear();
        recyclerView_PEND_AUD_LIST.removeAllViews();
        model_admin_pend_audit.clear();
        mAdapter_recycler_PEND_AUD_LIST.notifyDataSetChanged();
        Intent intent =  new Intent(getApplicationContext(),com.example.covidauditchecklists.admin.class);
        startActivity(intent);
        finish();
    }

    public  void emptyVisibility(){
        if(model_admin_pend_audit.size() == 0){
            pend_CheckList_Empty.setVisibility(View.VISIBLE);
        }else{
            pend_CheckList_Empty.setVisibility(View.INVISIBLE);
        }
    }
}