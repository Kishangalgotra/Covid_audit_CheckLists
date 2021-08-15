package com.example.covidauditchecklists;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import COMMON.FIREBASE_COMMON;
import COMMON.Flags;
import Controller_Classes.Activity_List_Of_CheckList;
import Model.Model;
import Model.MODEL_List_Of_CheckList;

public class Admin_Checklist extends AppCompatActivity implements Cutom_row_for_admin.onCustomClickListener  {

    public static ArrayList<String> positions_of_deleted_objects = new ArrayList<>();
    int lastindex ;
    int saveFlag = -1;
    static ArrayList<String>  admin_checked_data_for_delete;
    Button modify_checklist,save_checklist;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    static ArrayList<String> array;
    String[] array2;
    RecyclerView recyclerView;
    RecyclerViewAdapter_Admin recyclerViewAdapter_admin;
    private RecyclerView.Adapter mAdapter;
    private List<Model> mModelList;
    ArrayList<Integer> integre_array;
    int last_added_index;
    String commaseparatedlist = "";
    MODEL_List_Of_CheckList model_list_of_CHECK_ADM_checklist ;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menu1 = getMenuInflater();
        menu1.inflate(R.menu.add_checklist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.add_checklist) {
            final EditText edittext = new EditText(getApplicationContext());
            new AlertDialog.Builder(this).setView(edittext).setIcon(R.drawable.ic_baseline_check_circle_24)
                    .setTitle("CheckList Item Name").setMessage("Add Item")
                    .setPositiveButton("ADD", (dialog, which) -> {
                        if(!edittext.getText().toString().equalsIgnoreCase("")){
                            if(Flags.firstTimeAddNewCheckList == 1){
                                array.clear();
                            }
                            Flags.firstTimeAddNewCheckList = 0;
                            mModelList.add(new Model(edittext.getText().toString()));
                            array.add(edittext.getText().toString());
                            Log.i("ADDED",array.get(array.size()-1));
                            //lastindex++;
                            StringBuilder str = new StringBuilder("");
                            for (String eachstring : array) {
                                str.append(eachstring).append(",");
                            }
                            commaseparatedlist = str.toString();
                            Toast.makeText(this, "Item CheckList Added", Toast.LENGTH_SHORT).show();
                            mAdapter.notifyDataSetChanged();
                            saveFlag = 1;
                            save_checklist.setEnabled(true);
                            save_checklist.setBackgroundResource(R.drawable.three_choice_beckground);
                        }else{
                            Snackbar.make(findViewById(R.id.modify_checklist), "Please Give Some Value", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    }).setNegativeButton("Cancel", (dialog, which) -> {
                    }).show();
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_checklist);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

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
        admin_checked_data_for_delete = new ArrayList<>();
        Intent intent = getIntent();
        try{
            int size = intent.getStringArrayExtra("string-array").length;
            lastindex = size-1;
            array = new ArrayList<>();
            array2 = intent.getStringArrayExtra("string-array");

        }catch( NullPointerException e){
            Log.i("Exception",e.toString());
        }
        if(Activity_List_Of_CheckList.modificationIndex != -1){
            commaseparatedlist  =  Activity_List_Of_CheckList.model_list_of_checkLists.get(Activity_List_Of_CheckList.modificationIndex).getCheckList();
        }
        //RECYCLE VIEW DECLARATION AND INITIALIZATION
        Collections.addAll(array,array2);
        recyclerview_spacer recyclerview_spacer = new recyclerview_spacer(20);
        recyclerView = (RecyclerView) findViewById(R.id.admin_linearLayout_checklist);
        recyclerView.addItemDecoration(recyclerview_spacer);
        mAdapter = new RecyclerViewAdapter_Admin(getListData());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //MODIFY CHECKLIST WHEN ADMIN CLICK ON DELETE
        modify_checklist = findViewById(R.id.modify_checklist);
        modify_checklist.setOnClickListener(v -> {
            final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.anim);
            modify_checklist.startAnimation(myAnim);
            if(!admin_checked_data_for_delete.isEmpty()) {
                Collections.sort(admin_checked_data_for_delete);
                Collections.reverse(admin_checked_data_for_delete);
                for (String s : admin_checked_data_for_delete) {
                    array.remove(s);
                    System.out.println(Arrays.asList(array).toString());
                }

                integre_array = new ArrayList<>();
                for (String i : positions_of_deleted_objects) {
                    integre_array.add(Integer.parseInt(i));
                }

                Collections.sort(integre_array);
                System.out.println(Arrays.asList(integre_array).toString());
                Collections.reverse(integre_array);
                System.out.println(Arrays.asList(integre_array).toString());
                for (int i : integre_array) {
                    mModelList.remove(i);
                }
                integre_array.clear();
                admin_checked_data_for_delete.clear();
                positions_of_deleted_objects.clear();
                recyclerView.removeAllViews();
                StringBuilder str = new StringBuilder("");
                for (String eachstring : array) {
                    str.append(eachstring).append(",");
                }
                commaseparatedlist = str.toString();
                saveFlag = 1;
                save_checklist.setEnabled(true);
                save_checklist.setBackgroundResource(R.drawable.three_choice_beckground);
                Toast.makeText(getApplicationContext(), "CheckList Modified", Toast.LENGTH_SHORT).show();
                mAdapter.notifyDataSetChanged();
            }else{
                Toast.makeText(getApplicationContext(), "Please Select Item", Toast.LENGTH_SHORT).show();
            }
        });
        //SAVE CHECKLIST DATA IN FIREBASE
        save_checklist = findViewById(R.id.save_checklist);
        save_checklist.setOnClickListener(v -> {
            prepareCheckListData(v);
            saveFlag = -1;
        });
    }

    private List<Model> getListData() {
        mModelList = new ArrayList<>();
        add_in_model_array(array2);
        return mModelList;
    }

    //FILLING ARRAY WITH MODEL OBJECTS DATA
    public void add_in_model_array(String[] data){
        for(int i=0; i<data.length; i++){
            if( Flags.firstTimeAddNewCheckList  != 1){
                mModelList.add(new Model(data[i]));
            }
        }
    }

    //NOT USABLE IN CODE
    @Override
    public void onCustomCheckBoxClickListener(int position, CheckBox checkBox) {
        Log.i("Data :: ","Position");
       if(checkBox.isChecked()){
           admin_checked_data_for_delete.add(Integer.toString(position));
        }else {
           admin_checked_data_for_delete.remove(Integer.toString(position));
        }
    }

    @Override
    public void onCustomViewClickListener(int position, CheckBox checkBox) {
        Log.i("Data :: ","position");
        if(checkBox.isChecked()){
            admin_checked_data_for_delete.remove(Integer.toString(position));
            checkBox.setChecked(false);
        }else {
            admin_checked_data_for_delete.add(Integer.toString(position));
            Toast.makeText(this, Arrays.toString(new ArrayList[]{admin_checked_data_for_delete}),Toast.LENGTH_SHORT).show();
            checkBox.setChecked(true);
        }
    }

    public void prepareCheckListData(View v){
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.anim);
        save_checklist.startAnimation(myAnim);
        if(!commaseparatedlist.equalsIgnoreCase("")){
            if(!Activity_List_Of_CheckList.isCaseForModification) {
                last_added_index = Activity_List_Of_CheckList.newCheckListIndex;
                model_list_of_CHECK_ADM_checklist = Activity_List_Of_CheckList.model_list_of_checkLists.get(last_added_index);
                model_list_of_CHECK_ADM_checklist.setCheckList(commaseparatedlist);
                model_list_of_CHECK_ADM_checklist.setIsSelected(false);
                FIREBASE_COMMON.ADD_CHECKLIST_IN_LIST_OF_CHECKLISTS(recyclerView.getRootView(), model_list_of_CHECK_ADM_checklist);
            }else{
                model_list_of_CHECK_ADM_checklist = Activity_List_Of_CheckList.model_list_of_checkLists.get(Activity_List_Of_CheckList.modificationIndex);
                model_list_of_CHECK_ADM_checklist.setCheckList(commaseparatedlist);
                model_list_of_CHECK_ADM_checklist.setIsSelected(false);
                FIREBASE_COMMON.ADD_CHECKLIST_IN_LIST_OF_CHECKLISTS(recyclerView.getRootView(), model_list_of_CHECK_ADM_checklist);
                save_checklist.setEnabled(false);
                save_checklist.setBackgroundResource(R.drawable.custom_input);
            }
        }else{
            Snackbar.make(v, "Please Add At least One Item", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent =  new Intent(getApplicationContext(),Activity_List_Of_CheckList.class);
        Flags.firstTimeAddNewCheckList = 0;
        if(saveFlag == 1 ) {
            new AlertDialog.Builder(this).setIcon(R.drawable.ic_baseline_check_circle_24)
               .setTitle("You have not Saved Changes").setPositiveButton("Its Okay", (dialog, which) -> {
                super.onBackPressed();

                Activity_List_Of_CheckList.isCaseForModification = false;
                Activity_List_Of_CheckList.modificationIndex = -1;
                Activity_List_Of_CheckList.list_of_checklist_list.removeAllViews();
                Activity_List_Of_CheckList.mAdapter_list_of_checklist_list.notifyDataSetChanged();
                Activity_List_Of_CheckList.model_list_of_checkLists.clear();
                startActivity(intent);
                finish();

            }).setNegativeButton("My Bad", (dialog, which) -> {
            }).show();
        }
        if(saveFlag == -1){
            Activity_List_Of_CheckList.isCaseForModification = false;
            Activity_List_Of_CheckList.modificationIndex = -1;
            Activity_List_Of_CheckList.list_of_checklist_list.removeAllViews();
            Activity_List_Of_CheckList.mAdapter_list_of_checklist_list.notifyDataSetChanged();
            Activity_List_Of_CheckList.model_list_of_checkLists.clear();
            super.onBackPressed();
            startActivity(intent);
            finish();
        }
    }
}