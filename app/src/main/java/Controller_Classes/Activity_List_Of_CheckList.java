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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covidauditchecklists.Admin_Checklist;
import com.example.covidauditchecklists.R;
import com.example.covidauditchecklists.recyclerview_spacer;
import com.example.covidauditchecklists.user;
import com.firebase.ui.database.FirebaseIndexArray;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import COMMON.FIREBASE_COMMON;
import COMMON.Flags;
import Model.MODEL_ADMIN_PEND_AUDIT_REQ;
import Model.MODEL_List_Of_CheckList;
import Model.Model;
import Recyclerview_Classes.RecyclerView_List_of_CheckLists;
import Recyclerview_Classes.RecyclerView_Pending_aud_list;

public class Activity_List_Of_CheckList extends AppCompatActivity {

    public static int selectedAsMain = -1;
    public static int selectedMain = -1;
    public static boolean isCaseForModification = false;
    public static int modificationIndex = -1;
    //RECYCLERVIEW OBJECT DECLARATION
    public static RecyclerView list_of_checklist_list ;
    public static  RecyclerView.Adapter mAdapter_list_of_checklist_list ;
    public static int fetch_list_checklist = 0;
    //COMPONENTS DECLARATION
    public  Button list_of_checklist_select_main,list_of_checklist_delete,list_of_checklist_Modify,list_of_checklist_process;
    ProgressBar progressBar_list_of_checklist;
    TextView empty;
    //OBJECT DECLARATION
    public static List<MODEL_List_Of_CheckList> model_list_of_checkLists  = new ArrayList<>();
    public static int newCheckListIndex ;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(Flags.auditorPickCheckListFlag == 0){
            MenuInflater menu1 = getMenuInflater();
            menu1.inflate(R.menu.add_checklist, menu);
            return true;
        }else{
         return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.add_checklist) {
            final EditText edittext = new EditText(getApplicationContext());
            new AlertDialog.Builder(this).setView(edittext).setIcon(R.drawable.ic_baseline_check_circle_24)
                    .setTitle("CheckList Name").setPositiveButton("ADD", (dialog, which) -> {
                        int modelSize = model_list_of_checkLists.size();
                        Flags.firstTimeAddNewCheckList = 1;
                        newCheckListIndex = modelSize;
                        model_list_of_checkLists.add(new MODEL_List_Of_CheckList("",
                       false,edittext.getText().toString(),""));
                        Log.i("ADDED",Integer.toString(model_list_of_checkLists.size()-1));
                        Intent intent = new Intent(getApplicationContext(), Admin_Checklist.class);
                        intent.putExtra("CheckList_name","");
                        String[] empty_array = {""};
                        intent.putExtra("string-array",empty_array);
                        startActivity(intent);
                        finish();
                    }).setNegativeButton("Cancel", (dialog, which) -> {
            }).show();
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_check_list);
        fetch_list_checklist = 1;
        FIREBASE_COMMON.FETCH_LIST_OF_CHECKLISTS(getBaseContext());
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

        progressBar_list_of_checklist = findViewById(R.id.progressBar_list_of_checklist);
        progressBar_list_of_checklist.setVisibility(View.GONE);

        empty =findViewById(R.id.CheckList_Empty);

        recyclerview_spacer recyclerview_spacer = new recyclerview_spacer(20);
        list_of_checklist_list = (RecyclerView) findViewById(R.id.list_of_checklist_list);
        list_of_checklist_list.addItemDecoration(recyclerview_spacer);

        mAdapter_list_of_checklist_list = new RecyclerView_List_of_CheckLists(getListData());
        list_of_checklist_list.setAdapter(mAdapter_list_of_checklist_list);
        list_of_checklist_list.setLayoutManager(new LinearLayoutManager(this));
        mAdapter_list_of_checklist_list.notifyDataSetChanged();
        emptyVisibility();

        //list_of_checklist_select_main BUTTON CLICK EVENT
        list_of_checklist_select_main = findViewById(R.id.list_of_checklist_select_main);
        list_of_checklist_select_main.setOnClickListener(v -> {
            FIREBASE_COMMON.removeListener();
            final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.anim);
            list_of_checklist_select_main.startAnimation(myAnim);
            if(modificationIndex != -1){
                if(modificationIndex != selectedAsMain && selectedAsMain != -1) {
                    for (MODEL_List_Of_CheckList model_list_of_checkList : model_list_of_checkLists) {
                        model_list_of_checkList.setIsSelected(false);
                    }
                    MODEL_List_Of_CheckList model_list_of_checkList = model_list_of_checkLists.get(selectedAsMain);
                    model_list_of_checkList.setMainList("false");
                    FIREBASE_COMMON.ADD_CHECKLIST_IN_LIST_OF_CHECKLISTS(list_of_checklist_select_main.getRootView(),model_list_of_checkList);
                    model_list_of_checkLists.set(selectedAsMain, model_list_of_checkList);

                    model_list_of_checkList = model_list_of_checkLists.get(modificationIndex);
                    model_list_of_checkList.setMainList("true");
                    model_list_of_checkLists.set(modificationIndex, model_list_of_checkList);

                    FIREBASE_COMMON.ADD_CHECKLIST_IN_LIST_OF_CHECKLISTS(list_of_checklist_select_main.getRootView(),model_list_of_checkList);
                    FIREBASE_COMMON.SET_MAIN_CHECKIST(model_list_of_checkList.getCheckList());
                    model_list_of_checkLists.clear();
                    modificationIndex = -1;
                }else{
                    Toast.makeText(getApplicationContext(),"Already Main",Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getApplicationContext(),"Select CheckList",Toast.LENGTH_SHORT).show();
            }

        });

        //DELETE BUTTON CLICK EVENT
        list_of_checklist_delete = findViewById(R.id.list_of_checklist_delete);
        list_of_checklist_delete.setOnClickListener(v ->{
            FIREBASE_COMMON.removeListener();
            final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.anim);
            list_of_checklist_delete.startAnimation(myAnim);
            if(modificationIndex != -1){
                MODEL_List_Of_CheckList model_list_of_checkList = model_list_of_checkLists.get(modificationIndex);
                String checklistName = model_list_of_checkList.getCheckListname();
                FIREBASE_COMMON.delete_checklist(checklistName);
                model_list_of_checkLists.remove(modificationIndex);
                mAdapter_list_of_checklist_list.notifyDataSetChanged();
                emptyVisibility();
            }else{
                Toast.makeText(getApplicationContext(),"Nothing Selected to Delete",Toast.LENGTH_SHORT).show();
            }
        });

        //MODIFY BUTTON CLICK EVENT
        list_of_checklist_Modify = findViewById(R.id.list_of_checklist_Modify);
        list_of_checklist_Modify.setOnClickListener(v ->{
            //REMOVE VALUE EVENT LISTNER...
            FIREBASE_COMMON.removeListener();
            final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.anim);
            list_of_checklist_Modify.startAnimation(myAnim);
            if(!model_list_of_checkLists.isEmpty() && modificationIndex !=-1) {
                isCaseForModification = true;
                MODEL_List_Of_CheckList model_list_of_checkList = model_list_of_checkLists.get(modificationIndex);
                String checklistforModification = model_list_of_checkList.getCheckList();
                String[] checklistarray = checklistforModification.split(",");
                Intent intent = new Intent(this, Admin_Checklist.class);
                intent.putExtra("string-array", checklistarray);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(getApplicationContext(),"Please Make a New CheckList",Toast.LENGTH_SHORT).show();
            }
        });

        //PROCEED BUTTON CLICK EVENT
        list_of_checklist_process = findViewById(R.id.list_of_checklist_process);
        list_of_checklist_process.setOnClickListener(v ->{
            if(Flags.auditorCheckListSelectedFlag == 1) {
                Intent intent = new Intent(this, user.class);
                String list = Flags.model_list_of_checkListFlag.getCheckList();
                intent.putExtra("string-array", list.split(","));
                startActivity(intent);
            }else{
                Toast.makeText(getApplicationContext(),"No CheckList Selected",Toast.LENGTH_SHORT).show();
            }
        });

        if(Flags.auditorClientChecklist == 1){
            list_of_checklist_process.setVisibility(View.VISIBLE);
            list_of_checklist_Modify.setVisibility(View.INVISIBLE);
            list_of_checklist_delete.setVisibility(View.INVISIBLE);
        }else{
            list_of_checklist_process.setVisibility(View.INVISIBLE);
        }
    }

    //GET LIST
    private List<MODEL_List_Of_CheckList> getListData() {
        emptyVisibility();
        return model_list_of_checkLists;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        int value = Flags.auditorClientChecklist;
        list_of_checklist_list.removeAllViews();
        model_list_of_checkLists.clear();
        mAdapter_list_of_checklist_list.notifyDataSetChanged();
        if( value == 1){
            FIREBASE_COMMON.removeListener();
            finish();
        }else {
            FIREBASE_COMMON.removeListener();
            Intent intent = new Intent(getApplicationContext(), com.example.covidauditchecklists.admin.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
      //  FIREBASE_COMMON.removeListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FIREBASE_COMMON.removeListener();
    }

    public void emptyVisibility(){
       /* if(model_list_of_checkLists.size() == 0){
            empty.setVisibility(View.VISIBLE);
        }else{
            empty.setVisibility(View.INVISIBLE);
        }*/
    }
}