package com.example.covidauditchecklists;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import COMMON.COMMON_DATA;
import COMMON.FIREBASE_COMMON;
import Model.Users_data;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Searched_user_Data extends AppCompatActivity {
    TextView name;
    ImageView image;
    ListView listView;
    String user_name;
    String star;
    String selected_items;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Users_data");
    FirebaseUser currentUser;
    private FirebaseAuth mAuth ;
    Users_data users_data;
    TextView request_audit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searched_user__data);
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
        MainActivity.WRONG_CREDENTIALS.setVisibility(View.INVISIBLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        name = findViewById(R.id.searched_user_name2);
        image = findViewById(R.id.Searched_user_stars2);
        listView = findViewById(R.id.Searched_user_listview2);
        request_audit=findViewById(R.id.requestaudit);
        Intent intent = getIntent();
        GET_CLIENT_NAME();
        int search_user_condition = intent.getIntExtra("searched_user_condition",2);

        //REQUEST CODE FOR AUDITING WRITTEN BELOW
        request_audit.setOnClickListener(v -> {
            final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.anim);
            request_audit.startAnimation(myAnim);
            if(!COMMON_DATA.sNAME_common.equalsIgnoreCase("")){
                Intent intent1 = new Intent(getApplicationContext(),user_request.class);
                intent1.putExtra("User_name","TEMPORARY NAME");//users_data.getName());
                startActivity(intent1);
            }
        });

        if (search_user_condition == 1) {
            String ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child("Users_data").child(ID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot ds) {
                    if(ds.exists()) {
                        users_data = new Users_data(ds.child("name").getValue().toString(),ds.child("rating").getValue().toString(),
                                ds.child("selected_items").getValue().toString());
                        String[] list_data = users_data.getSelected_items().split(",");
                        ArrayAdapter<String> array = new ArrayAdapter<>(getApplicationContext(),
                                android.R.layout.simple_list_item_1, list_data);
                        listView.setAdapter(array);
                        int i = Integer.parseInt(users_data.getRating());
                        image.setImageResource(R.drawable.one_star);
                        give_star(i);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        } else {
            String user_data = intent.getStringExtra("user_data");
            String[] data_array = user_data.split("#");
            String[] list_data = data_array[2].split(",");
            name.setText(data_array[0].toUpperCase());
            ArrayAdapter<String> array = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list_data);
            listView.setAdapter(array);
            int i = Integer.parseInt(data_array[1]);
            image.setImageResource(R.drawable.one_star);
            give_star(i);
        }

        if(!COMMON_DATA.sNAME_common.equalsIgnoreCase(""))
            name.setText(COMMON_DATA.sNAME_common);
    }

    private void GET_CLIENT_NAME(){
        String ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Users").child(ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    COMMON_DATA.sNAME_common = snapshot.child("fullname").getValue(String.class);
                    if(!COMMON_DATA.sNAME_common.equalsIgnoreCase("")){
                        name.setText(COMMON_DATA.sNAME_common);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"FAILED",Toast.LENGTH_SHORT).show();
            }
        });
    }

    //STAR RATING RESULT
    public void give_star( int i){
        if (i == 0)
            image.setImageResource(R.drawable.zero_star);
        else if (i == 1)
            image.setImageResource(R.drawable.one_star);
        else if (i == 2)
            image.setImageResource(R.drawable.two_stars);
        else if (i == 3)
            image.setImageResource(R.drawable.three_stars);
        else if (i == 4)
            image.setImageResource(R.drawable.four_stars);
        else if (i == 5)
            image.setImageResource(R.drawable.five_stars);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setIcon(R.drawable.ic_baseline_exit)
                .setTitle("Are you Sure you want to Log Out ?").setPositiveButton("Yes", (dialog, which) -> {
            super.onBackPressed();
            Intent intent =  new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }).setNegativeButton("No", (dialog, which) -> {
        }).show();
    }
}