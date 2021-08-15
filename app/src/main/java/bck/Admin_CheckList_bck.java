package bck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.covidauditchecklists.Cutom_row_for_admin;
import com.example.covidauditchecklists.R;
import com.example.covidauditchecklists.RecyclerViewAdapter_Admin;
import com.example.covidauditchecklists.recyclerview_spacer;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import Model.Model;

class Admin_Checklist_bck extends AppCompatActivity implements Cutom_row_for_admin.onCustomClickListener  {

    //public static HashSet<Integer> positions_of_deleted_objects;
    public static ArrayList<String> positions_of_deleted_objects = new ArrayList<>();
    int lastindex ;
    static ArrayList<String>  admin_checked_data_for_delete;
    Button modify_checklist;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("CheckList");
    static ArrayList<String> array;
    String[] array2;
    // Button add_checklist;
    RecyclerView recyclerView;
    RecyclerViewAdapter_Admin recyclerViewAdapter_admin;
    private RecyclerView.Adapter mAdapter;
    private List<Model> mModelList;
    ArrayList<Integer> integre_array;

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
            new AlertDialog.Builder(this).setView(edittext).setIcon(android.R.drawable.ic_menu_add)
                    .setTitle("CheckList Name").setMessage("Add Item")
                    .setPositiveButton("ADD", (dialog, which) -> {
                        mModelList.add(new Model(edittext.getText().toString()));
                        array.add(edittext.getText().toString());
                        Log.i("ADDED",array.get(array.size()-1));
                        StringBuilder str = new StringBuilder("");
                        for (String eachstring : array) {
                            str.append(eachstring).append(",");
                        }
                        String commaseparatedlist = str.toString();
                        myRef.setValue(commaseparatedlist);
                        Toast.makeText(this, "Item CheckList Added", Toast.LENGTH_SHORT).show();
                        mAdapter.notifyDataSetChanged();
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
        int size = intent.getStringArrayExtra("string-array").length;
        array = new ArrayList<>();
        lastindex = size-1;
        array2 = intent.getStringArrayExtra("string-array");

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
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.anim);
        modify_checklist.setOnClickListener(v -> {
            Collections.sort(admin_checked_data_for_delete);
            Collections.reverse(admin_checked_data_for_delete);
            for (String s : admin_checked_data_for_delete) {
                array.remove(s);
                System.out.println(Arrays.asList(array).toString());
            }

            integre_array = new ArrayList<>();
            for(String i :positions_of_deleted_objects){
                integre_array.add(Integer.parseInt(i));
            }

            Collections.sort(integre_array);
            System.out.println(Arrays.asList(integre_array).toString());
            Collections.reverse(integre_array);
            System.out.println(Arrays.asList(integre_array).toString());
            for(int i :integre_array){
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
            String commaseparatedlist = str.toString();
            myRef.setValue(commaseparatedlist);
            Toast.makeText(getApplicationContext(), "CheckList Modified", Toast.LENGTH_SHORT).show();
            mAdapter.notifyDataSetChanged();
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
            mModelList.add(new Model(data[i]));
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent =  new Intent(getApplicationContext(),com.example.covidauditchecklists.admin.class);
        startActivity(intent);
        finish();
    }
}