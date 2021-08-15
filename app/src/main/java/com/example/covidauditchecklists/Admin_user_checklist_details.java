package com.example.covidauditchecklists;
import Model.Users_data;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Admin_user_checklist_details extends AppCompatActivity {

    AutoCompleteTextView searchView;
    ListView listView;
    private FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Users_data");
    FirebaseUser currentUser;
    ArrayAdapter listview_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_admin_user_checklist_details);
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
        searchView = findViewById(R.id.searchView);
        searchView.requestFocus();
        searchView.setFocusable(true);
        listView   = findViewById(R.id.listview);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.i("ValueEventListener","Inside");
                populateSearch(snapshot);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        myRef.addListenerForSingleValueEvent(valueEventListener);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String data2 = (String) listview_adapter.getItem(position);
            next(data2);
        });
    }

    public void next(String data){
        Intent intent = new Intent(this,Searched_user_Data.class);
        intent.putExtra("user_data",data);
        Log.i("data2",data);
        startActivity(intent);
    }

    private void populateSearch(DataSnapshot dataSnapshot) {
        ArrayList<String> array = new ArrayList<>();
        if(dataSnapshot.exists()){
            for(DataSnapshot ds :dataSnapshot.getChildren()){
                String name = ds.child("name").getValue(String.class);
                array.add(name);
            }
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,array);
            searchView.setAdapter(arrayAdapter);
            searchView.setOnItemClickListener((parent, view, position, id) -> {
            String name = searchView.getText().toString();
             searchUser(name);
            });
        }
    }

    private void searchUser(String name) {
        Query query = myRef.orderByChild("name").equalTo(name);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    ArrayList<String> user_fetch_data = new ArrayList<>();
                    for (DataSnapshot ds :snapshot.getChildren()){
                        Users_data users_data = new Users_data(ds.child("name").getValue().toString(),ds.child("rating").getValue()
                        .toString(),ds.child("selected_items").getValue().toString());
                        user_fetch_data.add(users_data.getName()+"#"+users_data.getRating()+"#"+users_data.getSelected_items());
                        listview_adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1,user_fetch_data);
                        listView.setAdapter(listview_adapter);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent =  new Intent(getApplicationContext(),com.example.covidauditchecklists.admin.class);
        startActivity(intent);
        finish();
    }
}