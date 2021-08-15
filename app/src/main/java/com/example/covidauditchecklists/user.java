package com.example.covidauditchecklists;

import android.content.DialogInterface;
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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import COMMON.COMMON_DATA;
import COMMON.Flags;
import Model.*;
import COMMON.FIREBASE_COMMON;
import Controller_Classes.auditor_client_list;
import Model.Model;
import Model.Users_data;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import Model.MODEL_AVAILABLE_AUDITORS;
import Recyclerview_Classes.RecyclerViewAdapter_Auditor_Client_List;

public class user extends AppCompatActivity implements custom_Row_for_User.onCustomClickListener {
    static ArrayList<String> admin_check_list;
    static ArrayList<String> user_checked_data;
    ImageView imageView1,imageView2,imageView3,imageView4,imageView5;
    RecyclerView recyclerView;
    int star_for_firebase =0;
    ProgressBar user_data_push_progress_bar;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Users_data");
    DatabaseReference myRef2 = database.getReference("CheckList");
    DatabaseReference myRef3 = database.getReference("Users");
    float check_star;
    private FirebaseAuth mAuth;
    Button checklist_submit,image_proof;
    String[] data;
    Window window;
    Button re_again;
    TextView tick_the_accesory_textview;
    LinearLayout linearLayout;
    ConstraintLayout constraintLayout;
    String username ;
    String[] array2;
    Animation myAnim1 = null;
    private RecyclerView.Adapter mAdapter;
    private List<Model> mModelList;
    public static  String USER_NAME;
    ValueEventListener fetchUserName,firebaseWork;

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
                    .setPositiveButton("Sign Out", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mAuth.signOut();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            finish();
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            }).show();
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
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
        recyclerView = findViewById(R.id.recyclerView_id);
        FirebaseWork();
        FIREBASE_COMMON.GET_AUDITOR_NAME();
        Intent intent = getIntent();
        constraintLayout = findViewById(R.id.user_constrain_layout);
        array2 = intent.getStringArrayExtra("string-array");
        user_data_push_progress_bar = findViewById(R.id.user_data_push_progress_bar);
        user_data_push_progress_bar.setVisibility(View.INVISIBLE);
        linearLayout = findViewById(R.id.star_linear_view);
        linearLayout.setVisibility(View.INVISIBLE);
        checklist_submit = findViewById(R.id.checklist_submit);
        image_proof = findViewById(R.id.image_proof);
        re_again = findViewById(R.id.re_again_fill_user_data);
        re_again.setVisibility(View.INVISIBLE);
        tick_the_accesory_textview = findViewById(R.id.tick_the_accesory_textview);
        re_again.setOnClickListener(v -> {
                tick_the_accesory_textview.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                mAdapter.notifyDataSetChanged();
                checklist_submit.setVisibility(View.VISIBLE);
                image_proof.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.INVISIBLE);
                re_again.setVisibility(View.INVISIBLE);
        });
        //FETCH USERNAME FROM FIREBASE
        fetchUserName = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        username = dataSnapshot.child("fullname").getValue(String.class);
                    }
                }
                @Override
                public void onCancelled(DatabaseError error) {
                    Log.w("TAG", "Failed to read value.", error.toException());
                }
        };
        myRef3.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(fetchUserName);
        myAnim1 = AnimationUtils.loadAnimation(this, R.anim.anim);
        //Pushing User data into Firebase
        checklist_submit.setOnClickListener(v -> {
           checklist_submit.startAnimation(myAnim1);
           StringBuilder str = new StringBuilder("");
           for (String eachstring : user_checked_data) {
               str.append(eachstring).append(",");
           }
           String commaseparatedlist = str.toString();
           int star_value = user_checked_data.size();
           STAR_CHANGE_FUNCTION(star_value);
           Users_data users_data = new Users_data(auditor_client_list.Clicked_user_Name,Integer.toString(star_for_firebase),commaseparatedlist );
           Users_data users_dataCheck1 = new Users_data("","0","");
           DatabaseReference databaseReference;
           databaseReference = FirebaseDatabase.getInstance().getReference("Users_data");
           databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
           databaseReference.child(auditor_client_list.Clicked_user_ID).
           setValue(users_dataCheck1).addOnCompleteListener(task -> {
                 if (task.isSuccessful()) {
                     //CODE FOR ADDING ENTRY IN PENDING AUDITOR REQUESTS
                     String AUDITOR_UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                     MODEL_ADMIN_PEND_AUDIT_REQ model_admin_pend_audit_req ;
                     model_admin_pend_audit_req = new MODEL_ADMIN_PEND_AUDIT_REQ(AUDITOR_UID, COMMON_DATA.sNAME_common,auditor_client_list.Clicked_user_ID,
                             auditor_client_list.Clicked_user_Name,users_data.getRating(),users_data.getSelected_items(),true);
                     model_admin_pend_audit_req.setRandomUID(Long.toString(new Date().getTime()));
                     FIREBASE_COMMON.ADD_ENTRY_IN_PEND_AUDIT_REQ(AUDITOR_UID,model_admin_pend_audit_req);
                     FIREBASE_COMMON.REMOVE_CLIENT_FROM_AUDITOR_LIST();
                     auditor_client_list.recyclerView_auditor_client.removeAllViews();
                     RecyclerViewAdapter_Auditor_Client_List.model_admin_auditors.remove(auditor_client_list.Clicked_user_Number);
                     //auditor_client_list.mAdapter_recyclerView_AUDITOR_CLIENTS.notifyDataSetChanged();
                     //------------------------------------------------------
                     user_data_push_progress_bar.setVisibility(View.INVISIBLE);
                     Log.i("Success", "user");
                     tick_the_accesory_textview.setVisibility(View.INVISIBLE);
                     recyclerView.setVisibility(View.INVISIBLE);
                     checklist_submit.setVisibility(View.INVISIBLE);
                     image_proof.setVisibility(View.INVISIBLE);
                     linearLayout.setVisibility(View.VISIBLE);
                     re_again.setVisibility(View.VISIBLE);
                 } else if (task.isCanceled()) {
                    // Toast.makeText(context.getApplicationContext(),"Uploading",Toast.LENGTH_LONG).show();
                 } else {
                     user_data_push_progress_bar.setVisibility(View.INVISIBLE);
                     Log.i("Failed", "user");
                 }
           });
        });

        image_proof.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(),image_proof.class));
        });
        admin_check_list = new ArrayList<>();
        try {
            Collections.addAll(admin_check_list, array2);
        }catch (Exception e){

        }
       user_checked_data = new ArrayList<>(admin_check_list.size());check_star = admin_check_list.size();
       imageView1 =  findViewById(R.id.first_star);
       imageView2 =  findViewById(R.id.second_star);
       imageView3 =  findViewById(R.id.third_star);
       imageView4 =  findViewById(R.id.fourth_star);
       imageView5 =  findViewById(R.id.fifth_star);
       recyclerview_spacer recyclerview_spacer = new recyclerview_spacer(20);
       recyclerView.addItemDecoration(recyclerview_spacer);
       mAdapter = new RecyclerViewAdapter(getListData());
       recyclerView.setAdapter(mAdapter);
       recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private List<Model> getListData() {
        mModelList = new ArrayList<>();
        add_in_model_array(array2);
        return mModelList;
    }

    public void add_in_model_array(String[] data){
        for(int i=0; i<data.length; i++){
            mModelList.add(new Model(data[i]));
        }
    }

    @Override
    public void onCustomCheckBoxClickListener(int position,CheckBox checkBox) {
    }

    @Override
    public void onCustomViewClickListener(int position, CheckBox checkBox) {
    }

    //FUNCTION FOR SHOWING STARS
    private void STAR_CHANGE_FUNCTION(float size1){
        float admin_size =admin_check_list.size();
        float size = size1/admin_size;
        size = size*100;
        Log.i("WAZesxrdctfvyg nbhyvt",Float.toString(size));
        if (size < 5){
            star_for_firebase =0;
            imageView1.setImageResource(R.drawable.ic_baseline_star_without_color);
            imageView2.setImageResource(R.drawable.ic_baseline_star_without_color);
            imageView3.setImageResource(R.drawable.ic_baseline_star_without_color);
            imageView4.setImageResource(R.drawable.ic_baseline_star_without_color);
            imageView5.setImageResource(R.drawable.ic_baseline_star_without_color);
        }else if(size >= 90){
            star_for_firebase =5;
            imageView1.setImageResource(R.drawable.ic_baseline_star_with_color);
            imageView2.setImageResource(R.drawable.ic_baseline_star_with_color);
            imageView3.setImageResource(R.drawable.ic_baseline_star_with_color);
            imageView4.setImageResource(R.drawable.ic_baseline_star_with_color);
            imageView5.setImageResource(R.drawable.ic_baseline_star_with_color);
        }
       else if(size >= 70){
            star_for_firebase =4;
            imageView1.setImageResource(R.drawable.ic_baseline_star_with_color);
            imageView2.setImageResource(R.drawable.ic_baseline_star_with_color);
            imageView3.setImageResource(R.drawable.ic_baseline_star_with_color);
            imageView4.setImageResource(R.drawable.ic_baseline_star_with_color);
            imageView5.setImageResource(R.drawable.ic_baseline_star_without_color);
        } else if(size >= 50){
            star_for_firebase =3;
            imageView1.setImageResource(R.drawable.ic_baseline_star_with_color);
            imageView2.setImageResource(R.drawable.ic_baseline_star_with_color);
            imageView3.setImageResource(R.drawable.ic_baseline_star_with_color);
            imageView4.setImageResource(R.drawable.ic_baseline_star_without_color);
            imageView5.setImageResource(R.drawable.ic_baseline_star_without_color);
        }else if(size >= 30){
            star_for_firebase =2;
            imageView1.setImageResource(R.drawable.ic_baseline_star_with_color);
            imageView2.setImageResource(R.drawable.ic_baseline_star_with_color);
            imageView3.setImageResource(R.drawable.ic_baseline_star_without_color);
            imageView4.setImageResource(R.drawable.ic_baseline_star_without_color);
            imageView5.setImageResource(R.drawable.ic_baseline_star_without_color);
        }else if(size >= 5){
            star_for_firebase =1;
            imageView1.setImageResource(R.drawable.ic_baseline_star_with_color);
            imageView2.setImageResource(R.drawable.ic_baseline_star_without_color);
            imageView3.setImageResource(R.drawable.ic_baseline_star_without_color);
            imageView4.setImageResource(R.drawable.ic_baseline_star_without_color);
            imageView5.setImageResource(R.drawable.ic_baseline_star_without_color);
        }

    }

    public void FirebaseWork(){
       firebaseWork =new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                data = dataSnapshot.getValue(String.class).split(",");
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        };
        myRef2.addValueEventListener(firebaseWork);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        myRef3.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeEventListener(fetchUserName);
        myRef2.removeEventListener(firebaseWork);
    }
}