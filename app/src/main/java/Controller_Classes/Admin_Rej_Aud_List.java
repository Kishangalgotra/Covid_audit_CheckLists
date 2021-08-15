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
import android.widget.Button;
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
import Recyclerview_Classes.RecyclerView_Pending_aud_list;

public class Admin_Rej_Aud_List extends AppCompatActivity {

    //RECYCLERVIEW OBJECT DECLARATION
    RecyclerView recyclerView_ADM_REJ_AUD_LIST ;
    public static  RecyclerView.Adapter mAdapter_ADM_REJ_AUD_LIST ;
    //COMPONENTS DECLARATION
    static ProgressBar progressBar_ADM_REJ_AUD_LIST ;
    //OBJECT DECLARATION
    public static  List<MODEL_ADMI_REJECTED_LIST> mModelList_ADM_REJ_AUD_LIST = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_rej_aud_list);
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

        progressBar_ADM_REJ_AUD_LIST = findViewById(R.id.progressBar_rejected_list);
        progressBar_ADM_REJ_AUD_LIST.setVisibility(View.GONE);

        recyclerview_spacer recyclerview_spacer = new recyclerview_spacer(20);
        recyclerView_ADM_REJ_AUD_LIST = findViewById(R.id.admin_rejected_recycler_list);
        recyclerView_ADM_REJ_AUD_LIST.addItemDecoration(recyclerview_spacer);

        mAdapter_ADM_REJ_AUD_LIST = new RecyclerView_ADM_REJ_LIST(getListData());
        recyclerView_ADM_REJ_AUD_LIST.setAdapter(mAdapter_ADM_REJ_AUD_LIST);
        recyclerView_ADM_REJ_AUD_LIST.setLayoutManager(new LinearLayoutManager(this));
        mAdapter_ADM_REJ_AUD_LIST.notifyDataSetChanged();

        FIREBASE_COMMON.FETCH_REJECTED_AUD_LIST(getBaseContext());
    }

    private List<MODEL_ADMI_REJECTED_LIST> getListData() {
        return mModelList_ADM_REJ_AUD_LIST;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FIREBASE_COMMON.removeListenerRejected();
        recyclerView_ADM_REJ_AUD_LIST.removeAllViews();
        mModelList_ADM_REJ_AUD_LIST.clear();
        mAdapter_ADM_REJ_AUD_LIST.notifyDataSetChanged();
        Intent intent =  new Intent(getApplicationContext(),com.example.covidauditchecklists.admin.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FIREBASE_COMMON.removeListenerRejected();
    }
}